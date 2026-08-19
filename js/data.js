window.App = window.App || {};

App.STOCK_MINIMO = 5;

const { Producto } = App.models;

App.state = {
  categorias:['Figuras','Máscaras','Indumentaria','Posters','Cómics','Accesorios'],

  productos:[
    new Producto({id:1,  nombre:'Figura Arácnido Urbano 18cm',        descripcion:'Figura de acción articulada, edición "Nueva Era", con base de exhibición.', precio:52000, categoria:'Figuras',      icono:'figura', stock:9}),
    new Producto({id:2,  nombre:'Máscara Trepamuros NY',              descripcion:'Réplica de tela con ojos reflectantes, talle único ajustable.',              precio:31000, categoria:'Máscaras',     icono:'mascara', stock:4}),
    new Producto({id:3,  nombre:'Remera "Vecino Arácnido" Roja',      descripcion:'Remera de algodón 100%, estampa texturada tipo tela de araña.',              precio:19000, categoria:'Indumentaria', icono:'remera', stock:16}),
    new Producto({id:4,  nombre:'Remera "Vecino Arácnido" Azul',      descripcion:'Versión azul de la remera insignia, corte unisex.',                          precio:19000, categoria:'Indumentaria', icono:'remera', stock:11}),
    new Producto({id:5,  nombre:'Poster "Skyline Nocturno" A2',       descripcion:'Ilustración de la ciudad al atardecer, impresión mate edición limitada.',    precio:14000, categoria:'Posters',      icono:'poster', stock:2}),
    new Producto({id:6,  nombre:'Cómic #1 — "Regreso al Barrio"',     descripcion:'Primer número de la saga independiente inspirada en el estreno del año.',    precio:8500,  categoria:'Cómics',       icono:'comic', stock:23}),
    new Producto({id:7,  nombre:'Mochila Trepamuros',                 descripcion:'Mochila resistente al agua con clips estilo "lanza-telarañas".',            precio:38000, categoria:'Accesorios',   icono:'mochila', stock:7}),
    new Producto({id:8,  nombre:'Taza "Gran Poder"',                  descripcion:'Taza cerámica 350ml, cambia de color con líquidos calientes.',               precio:9500,  categoria:'Accesorios',   icono:'taza', stock:0}),
    new Producto({id:9,  nombre:'Llavero Lanza-Telarañas',            descripcion:'Llavero holográfico con efecto de telaraña al moverlo.',                     precio:6000,  categoria:'Accesorios',   icono:'llavero', stock:3}),
    new Producto({id:10, nombre:'Figura Chibi Arácnido Coleccionable',descripcion:'Versión mini estilo chibi, ideal para escritorio o vitrina.',                precio:21000, categoria:'Figuras',      icono:'figura', stock:14}),
  ],

  carrito:[],
  pedidos:[],
  usuario:'katherine.cobos',

  vista:'inicio',
  subProductos:'listar',
  seleccionId:null,
  busqueda:'',

  nextProductId:11,
  nextPedidoId:1,
};
