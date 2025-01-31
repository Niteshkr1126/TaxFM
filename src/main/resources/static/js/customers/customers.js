// Initialize application
document.addEventListener('DOMContentLoaded', () => {
    // Initialize all components
    const tableManager = new TableManager();
    const formValidator = new FormValidator();
    const fileExporter = new FileExporter();

    tableManager.init();
    formValidator.init();
    fileExporter.init();
});

class TableManager {
  constructor() {
    this.search = document.querySelector('.input-group input');
    this.table = document.querySelector('#customers_table');
    this.tableRows = Array.from(document.querySelectorAll('tbody tr'));
    this.tableHeadings = document.querySelectorAll('thead th');
    this.init();
  }

  init() {
    this.setupSearch();
    this.setupSorting();
  }

  setupSearch() {
    this.search?.addEventListener('input', () => this.searchTable());
  }

  searchTable() {
    const searchValue = this.search.value.toLowerCase();

    this.tableRows.forEach((row, index) => {
      const rowData = row.textContent.toLowerCase();
      const isVisible = rowData.includes(searchValue);

      row.classList.toggle('hide', !isVisible);
      row.style.setProperty('--delay', `${index / 25}s`);
    });

    this.highlightVisibleRows();
  }

  highlightVisibleRows() {
    document.querySelectorAll('tbody tr:not(.hide)').forEach((row, index) => {
      row.style.backgroundColor = index % 2 === 0 ? 'transparent' : '#0000000b';
    });
  }

  setupSorting() {
    this.tableHeadings.forEach((head, index) => {
      let sortAsc = true;
      head.addEventListener('click', () => this.handleSortClick(head, index, sortAsc));
    });
  }

  handleSortClick(head, columnIndex, sortAsc) {
    this.tableHeadings.forEach(h => h.classList.remove('active'));
    head.classList.add('active');
    this.sortTable(columnIndex, sortAsc);
    head.classList.toggle('asc', sortAsc);
    sortAsc = !sortAsc;
  }

  sortTable(columnIndex, sortAsc) {
    const sortedRows = [...this.tableRows].sort((a, b) => {
      const aValue = a.querySelectorAll('td')[columnIndex].textContent.trim();
      const bValue = b.querySelectorAll('td')[columnIndex].textContent.trim();
      return this.compareValues(aValue, bValue, sortAsc);
    });

    sortedRows.forEach(row => this.table.querySelector('tbody').appendChild(row));
  }

  compareValues(a, b, sortAsc) {
    const numericCompare = !isNaN(a) && !isNaN(b);
    if (numericCompare) {
      return sortAsc ? a - b : b - a;
    }
    return sortAsc ? a.localeCompare(b) : b.localeCompare(a);
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
    this.table = document.querySelector('#customers_table');
    this.setupExportButtons();
  }

  setupExportButtons() {
    document.querySelector('#toPDF')?.addEventListener('click', () => this.exportToPDF());
    document.querySelector('#toJSON')?.addEventListener('click', () => this.exportToJSON());
    document.querySelector('#toCSV')?.addEventListener('click', () => this.exportToCSV());
    document.querySelector('#toEXCEL')?.addEventListener('click', () => this.exportToExcel());
  }

  exportToPDF() {
    const printWindow = window.open();
    printWindow.document.write(`
      <link rel="stylesheet" href="style.css">
      <main class="table">${this.table.innerHTML}</main>
    `);
    setTimeout(() => {
      printWindow.print();
      printWindow.close();
    }, 400);
  }

  exportToJSON() {
    const data = this.parseTableData();
    this.downloadFile(JSON.stringify(data, null, 2), 'json');
  }

  exportToCSV() {
    const headings = this.getTableHeadings().join(',') + ',image name';
    const rows = this.getTableRows().map(row =>
      row.cells.map(c => c.text.replace(/,/g, '.')).join(',') + `,${row.image}`
    ).join('\n');
    this.downloadFile(`${headings}\n${rows}`, 'csv', 'customer orders');
  }

  exportToExcel() {
    const headings = this.getTableHeadings().join('\t') + '\timage name';
    const rows = this.getTableRows().map(row =>
      row.cells.map(c => c.text).join('\t') + `\t${row.image}`
    ).join('\n');
    this.downloadFile(`${headings}\n${rows}`, 'excel');
  }

  parseTableData() {
    return this.getTableRows().map(row => ({
      ...Object.fromEntries(row.cells.map((cell, i) => [
        this.getTableHeadings()[i], cell.text
      ])),
      image: row.image
    }));
  }

  getTableHeadings() {
    return Array.from(this.table.querySelectorAll('th')).map(th => {
      const text = th.textContent.trim();
      return text.split(' ').slice(0, -1).join(' ').toLowerCase();
    });
  }

  getTableRows() {
    return Array.from(this.table.querySelectorAll('tbody tr')).map(row => ({
      cells: Array.from(row.querySelectorAll('td')).map(td => ({
        text: td.textContent.trim(),
        element: td
      })),
      image: decodeURIComponent(row.querySelector('img')?.src || '')
    }));
  }

  downloadFile(data, type, filename = '') {
    const mimeTypes = {
      json: 'application/json',
      csv: 'text/csv',
      excel: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    };

    const blob = new Blob([data], { type: mimeTypes[type] });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');

    link.href = url;
    link.download = filename;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
  }
}