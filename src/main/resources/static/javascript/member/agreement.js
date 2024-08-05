document.addEventListener('DOMContentLoaded', function () {
    var checkboxes = document.querySelectorAll('input[name="chk"]');
    var submitBtn = document.getElementById('submitBtn');

    checkboxes.forEach(function (checkbox) {
        checkbox.addEventListener('change', function () {
            var checkedCheckboxes = document.querySelectorAll('input[name="chk"]:checked');
            submitBtn.disabled = checkedCheckboxes.length !== checkboxes.length;
        });
    });
});