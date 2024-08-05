const findPwButton = document.getElementById('findPw');
const signInButton = document.getElementById('signIn');
const container = document.getElementById('container');

findPwButton.addEventListener('click', () => {
	container.classList.add("right-panel-active");
});

signInButton.addEventListener('click', () => {
	container.classList.remove("right-panel-active");
});

const urlParams = new URLSearchParams(window.location.search);

const error = urlParams.get('error');
if (error) {
    alert("입력하신 ID와 이메일 조회 결과 \n해당 회원 정보를 찾을 수 없습니다.");
    window.location.href = '/member/login';
}

const incorrect = urlParams.get('incorrect');
if (incorrect) {
    alert("입력하신 이메일 조회 결과 \n해당 회원 정보를 찾을 수 없습니다.");
    window.location.href = '/member/login';
}

const success = urlParams.get('success');
if (success) {
    alert("이메일이 성공적으로 전송되었습니다!");
    window.location.href = '/member/login';
}

const wait = urlParams.get('wait');
if (wait === 'notActive') {
    alert('회원가입 승인 대기중입니다.');
    window.location.href = '/member/login'
}

const fail = urlParams.get('fail');
if (fail) {
    alert('로그인 실패입니다. \n아이디와 비밀번호를 정확히 입력해주세요.');
    window.location.href = '/member/login'
}