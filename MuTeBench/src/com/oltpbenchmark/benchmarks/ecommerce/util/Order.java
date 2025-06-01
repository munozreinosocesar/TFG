/*******************************************************************************
 * oltpbenchmark.com
 *  
 *  Project Info:  http://oltpbenchmark.com
 *  Project Members:  	Carlo Curino <carlo.curino@gmail.com>
 * 				Evan Jones <ej@evanjones.ca>
 * 				DIFALLAH Djellel Eddine <djelleleddine.difallah@unifr.ch>
 * 				Andy Pavlo <pavlo@cs.brown.edu>
 * 				CUDRE-MAUROUX Philippe <philippe.cudre-mauroux@unifr.ch>  
 *  				Yang Zhang <yaaang@gmail.com> 
 * 
 *  This library is free software; you can redistribute it and/or modify it under the terms
 *  of the GNU General Public License as published by the Free Software Foundation;
 *  either version 3.0 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  See the GNU Lesser General Public License for more details.
 ******************************************************************************/

 package com.oltpbenchmark.benchmarks.ecommerce.util;

 import java.util.List;
 
 public class Order {
 
	 public int usuarioId;
	 public int pedidoId;
	 public List<Integer> productos;
	 public double monto;
	 public String fecha;
 
	 public Order(int usuarioId, int pedidoId, List<Integer> productos, double monto, String fecha) {
		 this.usuarioId = usuarioId;
		 this.pedidoId = pedidoId;
		 this.productos = productos;
		 this.monto = monto;
		 this.fecha = fecha;
	 }
 }
 