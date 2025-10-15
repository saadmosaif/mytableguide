// Wait for DOM to be fully loaded
document.addEventListener('DOMContentLoaded', function() {
    // Apply status colors to reservation status cells
    applyStatusColors();
    
    // Initialize any forms with validation
    initForms();
    
    // Initialize date-time pickers if available
    initDateTimePickers();
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
