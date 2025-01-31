class FontAwesomeLoader {
  static load() {
    if (!document.querySelector('script[src*="fontawesome"]')) {
      const script = document.createElement('script');
      script.src = "https://kit.fontawesome.com/23f7a91253.js";
      script.crossOrigin = "anonymous";
      document.head.appendChild(script);
    }
  }
}

class TabSwitcher {
  constructor() {
    this.tabLinks = document.querySelectorAll('.tab-link');
    this.loginForms = document.querySelectorAll('.login');
  }

  init() {
    this.tabLinks.forEach(link => {
      link.addEventListener('click', (e) => this.handleTabClick(e));
    });
  }

  handleTabClick(event) {
    event.preventDefault();
    const tabId = event.currentTarget.getAttribute('href').replace('#', '');

    this.tabLinks.forEach(button => button.classList.remove('active'));
    this.loginForms.forEach(form => form.classList.remove('active'));

    event.currentTarget.classList.add('active');
    document.getElementById(tabId).classList.add('active');
  }
}

class SmoothScroller {
  constructor() {
    this.anchors = document.querySelectorAll('a[href^="#"]');
    this.topBanner = document.querySelector('.top-banner');
    this.navBar = document.querySelector('nav');
  }

  init() {
    this.anchors.forEach(anchor => {
      anchor.addEventListener('click', (e) => this.handleScroll(e));
    });
  }

  handleScroll(event) {
    event.preventDefault();
    const targetSelector = event.currentTarget.getAttribute('href');
    const target = document.querySelector(targetSelector);

    if (target) {
      const targetPosition = this.calculateTargetPosition(target);
      window.scrollTo({
        top: targetPosition,
        behavior: 'smooth'
      });
    }
  }

  calculateTargetPosition(target) {
    const topBannerHeight = this.topBanner ? this.topBanner.offsetHeight : 0;
    const navBarHeight = this.navBar ? this.navBar.offsetHeight : 0;
    return target.getBoundingClientRect().top + window.pageYOffset - (topBannerHeight + navBarHeight);
  }
}

class App {
  static init() {
    FontAwesomeLoader.load();
    new TabSwitcher().init();
    new SmoothScroller().init();
  }
}

// Initialize application when DOM is ready
document.addEventListener('DOMContentLoaded', App.init);