function addToCartAndConfirm() {
                const form = document.getElementById('add-to-cart-form');
                const action = form.getAttribute('action');

                fetch(action, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: new URLSearchParams(new FormData(form))
                })
                .then(response => {
                    if (response.ok) {
                        alert('상품이 장바구니에 추가되었습니다.');
                        if (confirm('장바구니로 이동하시겠습니까?')) {
                            window.location.href = '/cart/list';
                        }
                    } else {
                        alert('상품을 장바구니에 추가하는 데 실패했습니다.');
                    }
                })
                .catch(error => {
                    alert('상품을 장바구니에 추가하는 중 오류가 발생했습니다.');
                });
            }

            $(window).on('beforeunload', function() {
                sessionStorage.setItem('scrollPosition', $(window).scrollTop());
            });

            if (sessionStorage.getItem('scrollPosition')) {
                $(window).scrollTop(sessionStorage.getItem('scrollPosition'));
                sessionStorage.removeItem('scrollPosition');
            }