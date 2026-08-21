window.App = window.App || {};

App.utils = {

  fmtMoney(n){
    return new Intl.NumberFormat('es-AR',{style:'currency', currency:'ARS', maximumFractionDigits:0}).format(n);
  },

  fmtFecha(d){
    return new Intl.DateTimeFormat('es-AR',{dateStyle:'short', timeStyle:'short'}).format(new Date(d));
  },

  esc(s){
    return String(s).replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
  },

  toast(msg, type='ok'){
    const host = document.getElementById('toast-host');
    if(!host) return;
    const el = document.createElement('div');
    el.className = 'toast' + (type==='error' ? ' error' : '');
    el.textContent = msg;
    host.appendChild(el);
    setTimeout(()=> el.remove(), 3200);
  },

  findProducto(id){
    return App.state.productos.find(p => p.id === Number(id));
  },
  findPedido(id){
    return App.state.pedidos.find(p => p.id === Number(id));
  },

  cartCount(){
    return App.state.carrito.reduce((acc,l)=>acc + l.cantidad, 0);
  },
  cartTotal(){
    return App.state.carrito.reduce((acc,l)=>{
      const p = App.utils.findProducto(l.productoId);
      return acc + (p ? p.precio * l.cantidad : 0);
    }, 0);
  },

  stockLabel(p){
    if(p.stock === 0) return `<span class="web-tag agotado">sin stock</span>`;
    if(p.stock <= App.STOCK_MINIMO) return `<span class="web-tag bajo">stock: ${p.stock}</span>`;
    return `stock: ${p.stock}`;
  },

  estadoTag(estado){
    return `<span class="web-tag ${estado}">${estado}</span>`;
  },

  iconoCategoria(clave){
    const iconos = {
      figura: `<svg viewBox="0 0 64 64" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round"><circle cx="32" cy="16" r="9"/><path d="M32 25v20M20 34h24M22 45l10 8 10-8M20 34l-6-12M44 34l6-12"/></svg>`,
      mascara: `<svg viewBox="0 0 64 64" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round"><path d="M12 30c0-12 9-20 20-20s20 8 20 20-9 24-20 24-20-12-20-24Z"/><path d="M20 28l6 4-6 4M44 28l-6 4 6 4M8 24l8 2M56 24l-8 2M8 34l8-2M56 34l-8-2"/></svg>`,
      remera: `<svg viewBox="0 0 64 64" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" stroke-linejoin="round"><path d="M22 10 12 18l4 8 6-3v29h20V23l6 3 4-8-10-8-6 4h-8Z"/><path d="M28 22l4 6 4-6M20 30l24 0M18 40l28 0"/></svg>`,
      poster: `<svg viewBox="0 0 64 64" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" stroke-linejoin="round"><rect x="12" y="8" width="40" height="48" rx="2"/><path d="M12 40l10-10 8 6 10-14 12 18M24 20h.01"/></svg>`,
      comic: `<svg viewBox="0 0 64 64" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" stroke-linejoin="round"><rect x="10" y="10" width="44" height="44" rx="2"/><path d="M18 22h20M18 30l8-6 6 8 8-10 6 14"/></svg>`,
      mochila: `<svg viewBox="0 0 64 64" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" stroke-linejoin="round"><path d="M20 24v-4a12 12 0 0 1 24 0v4"/><rect x="14" y="24" width="36" height="32" rx="4"/><path d="M24 24v10h16V24M24 40h16"/></svg>`,
      taza: `<svg viewBox="0 0 64 64" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" stroke-linejoin="round"><path d="M14 18h28v22a10 10 0 0 1-10 10H24a10 10 0 0 1-10-10V18Z"/><path d="M42 24h4a8 8 0 0 1 0 16h-4"/></svg>`,
      llavero: `<svg viewBox="0 0 64 64" fill="none" stroke="currentColor" stroke-width="2.4" stroke-linecap="round" stroke-linejoin="round"><circle cx="20" cy="20" r="8"/><path d="M26 26 48 48M38 38l6-6M44 44l6-6"/></svg>`,
    };
    return iconos[clave] || iconos.figura;
  },
};
