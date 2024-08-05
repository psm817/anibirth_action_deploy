document.addEventListener('DOMContentLoaded', function () {
    // 모든 FAQ 질문에 대해 클릭 이벤트 추가
    const faqQuestions = document.querySelectorAll('.faq-question');

    faqQuestions.forEach(question => {
        question.addEventListener('click', function () {
            const answer = this.nextElementSibling;
            const arrow = this.querySelector('.arrow');

            if (answer.style.display === 'none' || answer.style.display === '') {
                answer.style.display = 'block';
                arrow.textContent = '▼';
            } else {
                answer.style.display = 'none';
                arrow.textContent = '▶';
            }
        });
    });
});
