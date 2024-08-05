// 회원정보 입력 안하면 alert
function submitWriteForm(form) {
    form.username.value = form.username.value.trim();

    if ( form.username.value.length == 0 ) {
        alert("ID를 입력해주세요.");
        form.username.focus();
        return;
    }

    form.password.value = form.password.value.trim();

    if ( form.password.value.length == 0 ) {
        alert("비밀번호를 입력해주세요.");
        form.password.focus();
        return;
    }

    form.password_confirm.value = form.password_confirm.value.trim();

    if ( form.password_confirm.value.length == 0 ) {
        alert("비밀번호를 확인해주세요.");
        form.password_confirm.focus();
        return;
    }

    if(form.password.value != form.password_confirm.value) {
        alert("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
        form.password.focus();
        return;
    }

    form.authority.value = form.authority.value.trim();

    if ( form.authority.value === "" ) {
        alert("권한을 선택해주세요.");
        form.authority.focus();
        return;
    }

    form.nickname.value = form.nickname.value.trim();

    if ( form.nickname.value.length == 0 ) {
        alert("활동명을 입력해주세요.");
        form.nickname.focus();
        return;
    }

    form.email.value = form.email.value.trim();

    if ( form.email.value.length == 0 ) {
        alert("이메일을 입력해주세요.");
        form.email.focus();
        return;
    }

    form.phone.value = form.phone.value.trim();

    if ( form.phone.value.length == 0 ) {
        alert("전화번호를 입력해주세요.");
        form.phone.focus();
        return;
    }

    form.address.value = form.address.value.trim();

    if ( form.address.value.length == 0 ) {
        alert("주소를 입력해주세요.");
        form.address.focus();
        return;
    }

    if (form.thumbnailImg.files.length === 0) {
        alert("프로필 사진을 선택해주세요.");
        form.thumbnailImg.focus();
        return;
    }

    if (confirm("회원가입을 진행하시겠습니까? \n보호소/기업 회원은 관리자의 승인까지 대기가 필요합니다.")) {
        form.submit();
    }
}

function validateForm() {
    const submitButton = document.getElementById("submitButton");
    const usernameInput = document.getElementById("username");
    // Get the value of the username input field
    const username = usernameInput.value.trim();
    // Check if the username is not empty
    if (username !== "") {
        // Send an AJAX request to the server to check for duplicate username
        fetch("/member/check-username?username=" + username)
            .then(response => response.json())
            .then(data => {
                if (data.exists) {
                    alert("이미 존재하는 아이디입니다.");
                } else {
                    alert("입력하신 아이디는 사용 가능합니다.");
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }
}