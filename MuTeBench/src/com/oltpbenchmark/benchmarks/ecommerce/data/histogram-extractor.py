#!/usr/bin/env python
# -*- coding: utf-8 -*-

import os
import sys
import argparse
from datetime import datetime
from pprint import pprint, pformat
from decimal import Decimal
import MySQLdb as mdb

class Histogram(object):
    def __init__(self):
        self.data = {}
    
    def put(self, x, delta=1):
        self.data[x] = self.data.get(x, 0) + delta
    
    def get(self, x):
        return self.data.get(x, 0)
    
    def toJava(self):
        output = ""
        for key in sorted(self.data.keys()):
            cnt = self.data[key]
            if isinstance(key, str):
                key = "\"%s\"" % (key.replace('"', '\\"'))
            output += "this.put(%s, %d);\n" % (key, cnt)
        return output

## ==============================================
## processTrace
## ==============================================
def processTrace(traceFile):
    user_ids = {}
    user_id_ctr = 1
    user_h = Histogram()
    page_h = Histogram()

    total = 0
    with open(traceFile, "r") as f:
        for line in f:
            fields = line.strip().split(" ")
            assert len(fields) == 4, line
            orig_user = int(fields[0])
            namespace = int(fields[1])
            title = fields[2]

            if orig_user == 0:
                new_user = orig_user
            else:
                new_user = user_ids.setdefault(orig_user, user_id_ctr)
                if new_user == user_id_ctr:
                    user_id_ctr += 1

            user_h.put(new_user)
            page_h.put(title)
            total += 1

    print("Anonymous Updates: %d / %d [%f]" % (user_h.get(0), total, user_h.get(0) / float(total)))

    updates_per_user = Histogram()
    for x, cnt in user_h.data.items():
        if x == 0:
            continue
        updates_per_user.put(cnt)

    pprint(updates_per_user.data)

## ==============================================
## extractHistograms
## ==============================================
def extractHistograms(histograms, tableName, len_fields=[], cnt_fields=[], custom_fields={}):
    all_fields = []
    if len_fields:
        all_fields.append(", ".join([ "LENGTH(%s) AS %s" % (x, x) for x in len_fields ]))
    if cnt_fields:
        all_fields.append(", ".join(cnt_fields))
    if custom_fields:
        all_fields.extend([f"{val} AS {key}" for key, val in custom_fields.items()])

    sql = "SELECT %s FROM %s" % (", ".join(all_fields), tableName)
    print(sql)
    c1.execute(sql)

    fields = len_fields + cnt_fields + list(custom_fields.keys())
    num_fields = len(fields)

    for row in c1:
        for i in range(num_fields):
            f = fields[i]
            histograms.setdefault(f, Histogram()).put(int(row[i]) if isinstance(row[i], Decimal) else row[i])

## ==============================================
## main
## ==============================================
if __name__ == '__main__':
    aparser = argparse.ArgumentParser()
    aparser.add_argument('--host', type=str, required=False, help='MySQL host name')
    aparser.add_argument('--name', type=str, required=False, help='MySQL database name')
    aparser.add_argument('--user', type=str, required=False, help='MySQL username')
    aparser.add_argument('--pass', type=str, required=False, help='MySQL password')
    aparser.add_argument('--trace', type=str, required=False, help='Trace file path')
    args = vars(aparser.parse_args())

    if args['trace']:
        processTrace(args['trace'])
        sys.exit(0)

    mysql_conn = mdb.connect(host=args['host'], db=args['name'], user=args['user'], passwd=args['pass'])
    c1 = mysql_conn.cursor()
    c2 = mysql_conn.cursor()

    histograms = {}

    ## USER ATTRIBUTES
    fields = ["user_name", "user_real_name"]
    sql = """
        SELECT %s, 
            (SELECT COUNT(rev_id) FROM revision WHERE rev_user = user_id) AS user_revisions,
            (SELECT COUNT(wl_title) FROM watchlist WHERE wl_user = user_id) AS user_watches
        FROM user GROUP BY user_id
    """ % ",".join(fields)

    c1.execute(sql)
    fields.extend(["user_revisions", "user_watches"])
    num_fields = len(fields)

    for row in c1:
        for i in range(num_fields):
            f = fields[i]
            histograms.setdefault(f, Histogram()).put(len(row[i]) if i < num_fields - 2 else int(row[i]))

    ## PAGE ATTRIBUTES
    extractHistograms(histograms, "page", ["page_title"], ["page_namespace", "page_restrictions", "page_counter"])

    ## REVISIONS PER PAGE
    sql = "SELECT COUNT(rev_id), rev_page FROM revision GROUP BY rev_page"
    c1.execute(sql)
    f = "rev_per_page"
    histograms[f] = Histogram()

    for row in c1:
        cnt = row[0]
        if cnt >= 10000:
            cnt = round(cnt / 1000) * 1000
        elif cnt >= 1000:
            cnt = round(cnt / 100) * 100
        elif cnt >= 100:
            cnt = round(cnt / 10) * 10
        histograms[f].put(int(cnt))

    ## REVISION SIZES PER PAGE
    sql = "SELECT DISTINCT page_id FROM page"
    c1.execute(sql)
    f = "rev_size_diff"
    histograms[f] = Histogram()

    for row in c1:
        last_len = None
        sql = """
            SELECT FLOOR(LENGTH(old_text)/100.0)*100
            FROM revision, text
            WHERE rev_page = %d AND rev_text_id = old_id
            ORDER BY rev_id ASC
        """ % row[0]

        c2.execute(sql)
        for row2 in c2:
            if last_len is not None:
                diff = last_len - int(row2[0])
                histograms[f].put(diff)
            last_len = int(row2[0])

    ## REVISION ATTRIBUTES
    extractHistograms(histograms, "revision", ["rev_comment"], ["rev_minor_edit"])

    ## TEXT ATTRIBUTES
    extractHistograms(histograms, "text", [], ["old_flags"], {"old_text": "ROUND(LENGTH(old_text)/100.0)*100"})

    c1.close()
    c2.close()

    for key in histograms:
        print(key)
        print(histograms[key].toJava())
