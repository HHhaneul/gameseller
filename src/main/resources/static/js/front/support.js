        window.addEventListener("DOMContentLoaded", function() {
            const questions = document.getElementsByClassName("question");
            for (const el of questions) {
                el.addEventListener("click", function() {
                    const answer = this.nextElementSibling;
                    answer.classList.toggle('dn');
                });
            }
        });