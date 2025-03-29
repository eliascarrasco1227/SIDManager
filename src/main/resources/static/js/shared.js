
// JavaScript para manejar el modal
//$(document).ready(function() {
window.addEventListener('load', function () {
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

