// 회원정보 입력 안하면 alert
function submitModifyForm(form) {
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

    form.submit();
}