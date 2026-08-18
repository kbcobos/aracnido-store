window.App = window.App || {};

App.models = {};

App.models.Producto = class Producto{
  constructor({id, nombre, descripcion, precio, categoria, icono, stock}){
    this.id = id;
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.precio = precio;
    this.categoria = categoria;
    this.icono = icono;
    this.stock = stock;
  }
};

App.models.LineaPedido = class LineaPedido{
  constructor(producto, cantidad){
    this.productoId = producto.id;
    this.nombre = producto.nombre;
    this.precioUnitario = producto.precio;
    this.cantidad = cantidad;
  }
  get subtotal(){ return this.precioUnitario * this.cantidad; }
};

App.ESTADOS = ['pendiente','confirmado','enviado','entregado','cancelado'];

App.models.Pedido = class Pedido{
  constructor(id, usuario, lineas){
    this.id = id;
    this.usuario = usuario;
    this.fecha = new Date();
    this.lineas = lineas;
    this.estado = 'pendiente';
  }
  get total(){ return this.lineas.reduce((acc,l)=>acc + l.subtotal, 0); }
};
