// Font Awesome script loading
const loadFontAwesome = () => {
    const script = document.createElement('script');
    script.src = "https://kit.fontawesome.com/23f7a91253.js";
    script.crossOrigin = "anonymous";
    document.head.appendChild(script);
};

// Tab switching functionality
function openTab(event, tabId) {
    event.preventDefault();
    document.querySelectorAll('.tab-link').forEach(button => button.classList.remove('active'));
    document.querySelectorAll('.login').forEach(form => form.classList.remove('active'));

    event.currentTarget.classList.add('active');
    document.getElementById(tabId).classList.add('active');
}

// Smooth scrolling functionality
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));

            if (target) {
                // Calculate offset based on top banner and nav bar height
                const topBannerHeight = document.querySelector('.top-banner').offsetHeight || 0;
                const navBarHeight = document.querySelector('nav').offsetHeight || 0;
                const totalOffset = topBannerHeight + navBarHeight;

                // Smooth scroll to the target with offset
                const targetPosition = target.getBoundingClientRect().top + window.pageYOffset - totalOffset;

                window.scrollTo({
                    top: targetPosition,
                    behavior: 'smooth',
                });
            }
        });
    });

// Initialize the script
loadFontAwesome();