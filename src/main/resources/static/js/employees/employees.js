// Initialize application
document.addEventListener('DOMContentLoaded', () => {
    // Initialize all components
    const tableManager = new TableManager();
    const formValidator = new FormValidator();
    const fileExporter = new FileExporter();
    const clickableRowManager = new ClickableRowManager();

    tableManager.init();
    formValidator.init();
    fileExporter.init();
    clickableRowManager.init();
});

class TableManager {
    constructor() {
        this.search = document.querySelector('.input-group input');
        this.tableRows = document.querySelectorAll('tbody tr');
        this.tableHeadings = document.querySelectorAll('thead th');
        this.customersTable = document.querySelector('#customers_table');
    }

    init() {
        if (this.search) {
            this.search.addEventListener('input', () => this.searchTable());
        }
        
        if (this.tableHeadings) {
            this.addSorting();
        }
    }

    searchTable() {
        this.tableRows.forEach((row, i) => {
            const tableData = row.textContent.toLowerCase();
            const searchData = this.search.value.toLowerCase();
            
            row.classList.toggle('hide', tableData.indexOf(searchData) < 0);
            row.style.setProperty('--delay', i / 25 + 's');
            
            this.highlightVisibleRows();
        });
    }

    highlightVisibleRows() {
        document.querySelectorAll('tbody tr:not(.hide)').forEach((visibleRow, i) => {
            visibleRow.style.backgroundColor = (i % 2 === 0) ? 'transparent' : '#0000000b';
        });
    }

    addSorting() {
        this.tableHeadings.forEach((head, i) => {
            let sortAsc = true;
            head.onclick = () => {
                this.tableHeadings.forEach(h => h.classList.remove('active'));
                head.classList.add('active');
                this.sortTable(i, sortAsc);
                head.classList.toggle('asc', sortAsc);
                sortAsc = !sortAsc;
            };
        });
    }

    sortTable(column, sortAsc) {
        const rowsArray = [...this.tableRows];
        
        rowsArray.sort((a, b) => {
            const firstRow = a.querySelectorAll('td')[column].textContent.trim();
            const secondRow = b.querySelectorAll('td')[column].textContent.trim();
            const isNumeric = !isNaN(firstRow) && !isNaN(secondRow);
            
            return sortAsc ? 
                (isNumeric ? parseFloat(firstRow) - parseFloat(secondRow) : firstRow.localeCompare(secondRow)) :
                (isNumeric ? parseFloat(secondRow) - parseFloat(firstRow) : secondRow.localeCompare(firstRow));
        });

        rowsArray.forEach(row => document.querySelector('tbody').appendChild(row));
    }
}

class FormValidator {
    constructor() {
        this.resetForm = document.getElementById('resetPasswordForm');
        this.passwordInput = document.getElementById('newPassword');
        this.confirmInput = document.getElementById('confirmPassword');
    }

    init() {
        if (this.resetForm) {
            // Add input event listeners for real-time validation
            this.passwordInput?.addEventListener('input', () => this.validatePasswords());
            this.confirmInput?.addEventListener('input', () => this.validatePasswords());

            this.resetForm.addEventListener('submit', (e) => this.handleFormSubmit(e));
        }
    }

    validatePasswords() {
        // Clear previous validity states
        this.confirmInput.setCustomValidity('');
        this.confirmInput.classList.remove('is-invalid');

        if (this.passwordInput.value !== this.confirmInput.value) {
            this.confirmInput.setCustomValidity("Passwords must match");
            this.confirmInput.classList.add('is-invalid');
            return false;
        }
        return true;
    }

    handleFormSubmit(event) {
        if (!this.validatePasswords()) {
            event.preventDefault();
            event.stopPropagation();
        }
        this.resetForm.classList.add('was-validated');
    }
}

class FileExporter {
    constructor() {
        this.customersTable = document.querySelector('#customers_table');
    }

    init() {
        if (this.customersTable) {
            this.setupExportButtons();
        }
    }

    setupExportButtons() {
        document.querySelector('#toPDF')?.addEventListener('click', () => this.toPDF());
        document.querySelector('#toJSON')?.addEventListener('click', () => this.toJSON());
        document.querySelector('#toCSV')?.addEventListener('click', () => this.toCSV());
        document.querySelector('#toEXCEL')?.addEventListener('click', () => this.toExcel());
    }

    toPDF() {
        const htmlCode = `
            <link rel="stylesheet" href="style.css">
            <main class="table">${this.customersTable.innerHTML}</main>
        `;
        const newWindow = window.open();
        newWindow.document.write(htmlCode);
        setTimeout(() => {
            newWindow.print();
            newWindow.close();
        }, 400);
    }

    toJSON() {
        const json = this.convertTableToJSON();
        this.downloadFile(json, 'json');
    }

    toCSV() {
        const csv = this.convertTableToCSV();
        this.downloadFile(csv, 'csv', 'customer orders');
    }

    toExcel() {
        const excel = this.convertTableToExcel();
        this.downloadFile(excel, 'excel');
    }

    convertTableToJSON() {
        const tableData = [];
        const tHeadings = Array.from(this.customersTable.querySelectorAll('th'))
            .map(head => head.textContent.trim().split(' ').slice(0, -1).join(' ').toLowerCase());

        this.customersTable.querySelectorAll('tbody tr').forEach(row => {
            const rowObject = {};
            row.querySelectorAll('td').forEach((cell, index) => {
                const img = cell.querySelector('img');
                rowObject[tHeadings[index]] = img ? decodeURIComponent(img.src) : cell.textContent.trim();
            });
            tableData.push(rowObject);
        });

        return JSON.stringify(tableData, null, 4);
    }

    convertTableToCSV() {
        const headings = this.getTableHeadings().join(',') + ',image name';
        const rows = Array.from(this.customersTable.querySelectorAll('tbody tr'))
            .map(row => this.processTableRow(row, ',')).join('\n');
        return headings + '\n' + rows;
    }

    convertTableToExcel() {
        const headings = this.getTableHeadings().join('\t') + '\timage name';
        const rows = Array.from(this.customersTable.querySelectorAll('tbody tr'))
            .map(row => this.processTableRow(row, '\t')).join('\n');
        return headings + '\n' + rows;
    }

    getTableHeadings() {
        return Array.from(this.customersTable.querySelectorAll('th'))
            .map(head => head.textContent.trim().split(' ').slice(0, -1).join(' ').toLowerCase());
    }

    processTableRow(row, delimiter) {
        const cells = Array.from(row.querySelectorAll('td'));
        const img = decodeURIComponent(row.querySelector('img')?.src || '');
        const data = cells.map(cell => cell.textContent.replace(/,/g, ".").trim()).join(delimiter);
        return data + delimiter + img;
    }

    downloadFile(data, fileType, fileName = '') {
        const mimeTypes = {
            'json': 'application/json',
            'csv': 'text/csv',
            'excel': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        };
        
        const a = document.createElement('a');
        a.download = fileName;
        a.href = `data:${mimeTypes[fileType]};charset=utf-8,${encodeURIComponent(data)}`;
        document.body.appendChild(a);
        a.click();
        a.remove();
    }
}

class ClickableRowManager {
    constructor() {
        this.init();
    }

    init() {
        // Attach event listener to the entire document to handle all tables
        document.addEventListener('click', (event) => {
            const row = event.target.closest('.clickable-row');
            if (row) {
                // Handle customers and subordinates dynamically
                const customerId = row.getAttribute('data-customer-id');
                const employeeId = row.getAttribute('data-employee-id');

                if (customerId) {
                    window.location.href = `/customers/${customerId}`;
                } else if (employeeId) {
                    window.location.href = `/employees/${employeeId}`;
                }
            }
        });
    }
}