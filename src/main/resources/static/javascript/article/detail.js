function confirmDelete(id) {
            if (confirm("정말 이 공지사항을 삭제하시겠습니까?")) {
                document.getElementById('delete-form-' + id).submit();
            }
        }