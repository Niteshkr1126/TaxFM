// Font Awesome script loading
const loadFontAwesome = () => {
    const script = document.createElement('script');
    script.src = "https://kit.fontawesome.com/23f7a91253.js";
    script.crossOrigin = "anonymous";
    document.head.appendChild(script);
};

// Tab switching functionality
function openTab(event, formId) {
    event.preventDefault(); // Prevent default behavior

    // Hide all login forms
    const loginForms = document.querySelectorAll('.login');
    loginForms.forEach(form => form.classList.remove('active'));

    // Show the selected form
    const selectedForm = document.getElementById(formId);
    selectedForm.classList.add('active');

    // Highlight the active tab button
    const tabLinks = document.querySelectorAll('.tab-link');
    tabLinks.forEach(tab => tab.classList.remove('active'));
    event.target.classList.add('active');
}

// On page load, set the active tab from localStorage or default to employee-login
document.addEventListener('DOMContentLoaded', () => {
    const activeTab = localStorage.getItem('activeTab') || 'employee-login';
    const activeButton = document.querySelector(`.tab-link[onclick*="${activeTab}"]`);
    if (activeButton) {
        activeButton.click();
    }
});


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