// Wait for DOM to be fully loaded
document.addEventListener('DOMContentLoaded', function() {
    // Apply status colors to reservation status cells
    applyStatusColors();

    // Initialize any forms with validation
    initForms();

    // Initialize date-time pickers if available
    initDateTimePickers();

    // Initialize luxurious UI enhancements
    initLuxuriousUI();
});

/**
 * Apply color classes to reservation status text
 */
function applyStatusColors() {
    document.querySelectorAll('td').forEach(cell => {
        const text = cell.textContent.trim().toUpperCase();
        if (text === 'PENDING') {
            cell.classList.add('status-pending');
        } else if (text === 'CONFIRMED') {
            cell.classList.add('status-confirmed');
        } else if (text === 'CANCELED') {
            cell.classList.add('status-canceled');
        }
    });
}

/**
 * Initialize form validation
 */
function initForms() {
    const forms = document.querySelectorAll('form');

    forms.forEach(form => {
        form.addEventListener('submit', function(event) {
            let isValid = true;

            // Validate required fields
            form.querySelectorAll('[required]').forEach(field => {
                if (!field.value.trim()) {
                    isValid = false;
                    field.classList.add('error');

                    // Create or update error message
                    let errorMsg = field.nextElementSibling;
                    if (!errorMsg || !errorMsg.classList.contains('error-message')) {
                        errorMsg = document.createElement('div');
                        errorMsg.classList.add('error-message');
                        field.parentNode.insertBefore(errorMsg, field.nextSibling);
                    }
                    errorMsg.textContent = 'This field is required';
                } else {
                    field.classList.remove('error');
                    const errorMsg = field.nextElementSibling;
                    if (errorMsg && errorMsg.classList.contains('error-message')) {
                        errorMsg.remove();
                    }
                }
            });

            // Validate email fields
            form.querySelectorAll('input[type="email"]').forEach(field => {
                if (field.value.trim() && !isValidEmail(field.value)) {
                    isValid = false;
                    field.classList.add('error');

                    // Create or update error message
                    let errorMsg = field.nextElementSibling;
                    if (!errorMsg || !errorMsg.classList.contains('error-message')) {
                        errorMsg = document.createElement('div');
                        errorMsg.classList.add('error-message');
                        field.parentNode.insertBefore(errorMsg, field.nextSibling);
                    }
                    errorMsg.textContent = 'Please enter a valid email address';
                }
            });

            if (!isValid) {
                event.preventDefault();
            }
        });
    });
}

/**
 * Initialize date-time pickers for reservation forms
 */
function initDateTimePickers() {
    // This is a placeholder for implementing date-time pickers
    // You would typically use a library like flatpickr or bootstrap-datepicker
    console.log('Date-time pickers would be initialized here');
}

/**
 * Validate email format
 */
function isValidEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

/**
 * Add a new row to a table dynamically
 */
function addTableRow(tableId, rowData) {
    const table = document.getElementById(tableId);
    if (!table) return;

    const tbody = table.querySelector('tbody') || table;
    const row = document.createElement('tr');

    rowData.forEach(cellData => {
        const cell = document.createElement('td');
        cell.textContent = cellData;
        row.appendChild(cell);
    });

    tbody.appendChild(row);
}

// Add CSS class for form validation
document.head.insertAdjacentHTML('beforeend', `
<style>
.error {
    border-color: #e74c3c !important;
}
.error-message {
    color: #e74c3c;
    font-size: 0.8rem;
    margin-top: -10px;
    margin-bottom: 10px;
}
</style>
`);

/**
 * Initialize luxurious UI enhancements
 */
function initLuxuriousUI() {
    // Create theme toggle button if it doesn't exist
    if (!document.querySelector('.theme-toggle')) {
        const themeToggle = document.createElement('div');
        themeToggle.className = 'theme-toggle';
        themeToggle.innerHTML = '<i class="fas fa-moon"></i>';
        document.body.appendChild(themeToggle);

        // Check for saved theme preference
        const savedTheme = localStorage.getItem('theme');
        if (savedTheme === 'dark') {
            document.body.classList.add('dark-mode');
            themeToggle.innerHTML = '<i class="fas fa-sun"></i>';
        }

        // Toggle theme on click
        themeToggle.addEventListener('click', function() {
            document.body.classList.toggle('dark-mode');

            if (document.body.classList.contains('dark-mode')) {
                localStorage.setItem('theme', 'dark');
                themeToggle.innerHTML = '<i class="fas fa-sun"></i>';
            } else {
                localStorage.setItem('theme', 'light');
                themeToggle.innerHTML = '<i class="fas fa-moon"></i>';
            }
        });
    }

    // Add animation to cards
    const cards = document.querySelectorAll('.card, .dashboard-card');
    cards.forEach(function(card, index) {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        card.style.transition = 'opacity 0.5s ease, transform 0.5s ease';

        setTimeout(function() {
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, 100 + (index * 100)); // Stagger the animations
    });

    // Add hover effect to tables
    const tableRows = document.querySelectorAll('tr');
    tableRows.forEach(function(row) {
        if (!row.closest('thead')) { // Skip header rows
            row.addEventListener('mouseenter', function() {
                this.style.transform = 'scale(1.01)';
                this.style.transition = 'transform 0.3s ease';
            });

            row.addEventListener('mouseleave', function() {
                this.style.transform = 'scale(1)';
            });
        }
    });

    // Add icon to status elements
    const statusElements = document.querySelectorAll('.status-pending, .status-confirmed, .status-canceled');
    statusElements.forEach(function(element) {
        let icon = '';
        if (element.classList.contains('status-pending')) {
            icon = '<i class="fas fa-clock mr-1"></i>';
        } else if (element.classList.contains('status-confirmed')) {
            icon = '<i class="fas fa-check-circle mr-1"></i>';
        } else if (element.classList.contains('status-canceled')) {
            icon = '<i class="fas fa-times-circle mr-1"></i>';
        }

        if (icon && !element.querySelector('i')) {
            element.innerHTML = icon + element.innerHTML;
        }
    });

    // Add luxurious hover effect to buttons
    const buttons = document.querySelectorAll('button, .btn, input[type="submit"]');
    buttons.forEach(function(button) {
        button.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-3px)';
            this.style.boxShadow = '0 5px 15px rgba(0, 0, 0, 0.2)';
        });

        button.addEventListener('mouseleave', function() {
            this.style.transform = '';
            this.style.boxShadow = '';
        });
    });
}
