window.App = window.App || {};

App.theme = {
  actual:'light',

  init(){
    this.apply('light');
  },

  toggle(){
    this.apply(this.actual === 'light' ? 'dark' : 'light');
  },

  apply(modo){
    this.actual = modo;
    document.documentElement.setAttribute('data-theme', modo === 'dark' ? 'dark' : 'light');
    const btn = document.getElementById('theme-toggle-btn');
    if(btn) btn.textContent = modo === 'dark' ? '☀️' : '🌙';
  },
};
