// 봉사후기 정보 입력 안하면 alert
function reviewSubmitButton(form) {
    form.title.value = form.title.value.trim();

    if ( form.title.value.length == 0 ) {
        alert("제목을 입력해주세요.");
        form.title.focus();
        return;
    }

    if (form.thumbnailImg.files.length === 0) {
        alert("대표 이미지를 선택해주세요.");
        form.thumbnailImg.focus();
        return;
    }

    form.body.value = form.body.value.trim();

    if ( form.body.value.length == 0 ) {
        alert("내용을 입력해주세요.");
        form.body.focus();
        return;
    }

    form.submit();
}

// 첨부이미지
function addImageInput() {
    // Get the current number of input boxes
    var imageContainer = document.getElementById('imageContainer');
    var inputCount = imageContainer.getElementsByClassName('input-box').length;

    // Check if the number of input boxes is less than 3
    if (inputCount < 3) {
        // Create a new div for the new input box
        var newInputBox = document.createElement('div');
        newInputBox.className = 'input-box';

        // Create the new input element
        var newInput = document.createElement('input');
        newInput.className = 'file-box';
        newInput.type = 'file';
        newInput.name = 'subImg';

        // Append the new input element to the new input box
        newInputBox.appendChild(newInput);

        // Append the new input box to the image container
        imageContainer.appendChild(newInputBox);
    } else {
        alert('첨부 이미지는 최대 3개까지 첨부할 수 있습니다.');
    }
}