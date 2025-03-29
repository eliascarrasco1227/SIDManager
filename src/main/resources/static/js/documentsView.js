$(document).ready(function() {
    var table = $('#example').DataTable();

    // Almacenar los datos originales para poder restaurarlos
    var originalData = table.rows().data().toArray();

    // Filtro para documentos "Not received"
    $('#filter-not-received').on('change', function() {
        table.column(4).search(this.checked ? 'Not received' : '').draw();
    });
	/*
    function filterTable(minDate, maxDate) {
        var table = $('#example').DataTable();
        table.clear();

        // Convertir las fechas a objetos Date
        var minDateObj = minDate ? new Date(minDate) : null;
        var maxDateObj = maxDate ? new Date(maxDate) : null;

        // Agregar de nuevo las filas que cumplan con el filtro
        originalData.forEach(function(row) {
            var rowDate = new Date(row[3]); // Obtener la fecha de la columna 3

            // Verificar condiciones según los parámetros de filtrado
            var includeRow = true;
            
            if (minDateObj && rowDate < minDateObj) {
                includeRow = false;
            } else if (maxDateObj && rowDate > maxDateObj) {
                includeRow = false;
            }
            
            if (includeRow) {
                table.row.add(row);
            }
        });

        table.draw();
    }

    // Filtro para documentos entre una fecha mínima o máxima
    $('#applyButton').on('click', function() {
        var minDate = $('#min-date').val();
        var maxDate = $('#max-date').val();

        if (minDate || maxDate) {
            filterTable(minDate, maxDate);
        } else {
            // Mostrar el modal de advertencia
            var modal = document.getElementById("myModal");
            var span = document.getElementsByClassName("close")[0];
            modal.style.display = "block";
            span.onclick = function() {
                modal.style.display = "none";
            }
            window.onclick = function(event) {
                if (event.target == modal) {
                    modal.style.display = "none";
                }
            }
        }
    });

    // Restablecer el filtro y mostrar todos los datos originales
    $('#resetButton').on('click', function() {
        table.clear();
        table.rows.add(originalData).draw();
    });
    */
    
});
		
		
// JavaScript para manejar el modal
$(document).ready(function() {
    var modal = $('#errorMessageModal');
    var span = $('.close-button');

    // Mostrar el modal si hay un mensaje de respuesta
    if ($('.modal').text().trim() !== "") {
        modal.show();
    }

    // Cerrar el modal al hacer clic en el botón "x"
    span.click(function() {
        modal.hide();
    });

    // Cerrar el modal al hacer clic fuera del contenido del modal
    $(window).click(function(event) {
        if (event.target == modal[0]) {
            modal.hide();
        }
    });
});

    