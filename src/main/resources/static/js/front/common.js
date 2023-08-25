window.addEventListener("DOMContentLoaded", function(){
    CKEDITOR.replace("p_content", {
        height: 350
    });
});

        ClassicEditor
            .create(document.querySelector('#p_content'))
            .catch(error => {
                console.error(error);
            });