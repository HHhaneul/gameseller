var commonLib = commonLib || {};

/**
* ajax 처리
*
* @param method : 요청 메서드 - GET, POST, PUT ...
* @param url : 요청 URL
* @param responseType : json - 응답 결과를 json 변환, 아닌 경우는 문자열로 반환
*/
commonLib.ajaxLoad = function(method, url, params, responseType) {
    method = !method || !method.trim()? "GET" : method.toUpperCase();
    const token = document.querySelector("meta[name='_csrf']").content;
    const header = document.querySelector("meta[name='_csrf_header']").content;
    const ctxPath = document.querySelector("meta[name='_ctx_path']").content;
    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        xhr.open(method, url);
        xhr.setRequestHeader(header, token);

        xhr.send(params);
        responseType = responseType?responseType.toLowerCase():undefined;
        if (responseType == 'json') {
            xhr.responseType=responseType;
        }

        xhr.onreadystatechange = function() {
            if (xhr.status == 200 && xhr.readyState == XMLHttpRequest.DONE) {
                const resultData = responseType == 'json' ? xhr.response : xhr.responseText;

                resolve(resultData);
            }
        };

        xhr.onabort = function(err) {
            reject(err);
        };

        xhr.onerror = function(err) {
            reject(err);
        };

        xhr.ontimeout = function(err) {
            reject(err);
        };
    });
}

window.addEventListener("DOMContentLoaded", function() {
    /** 양식 공통 처리 S */
    const formActions = document.getElementsByClassName("form_action");
    for (const el of formActions) {
        el.addEventListener("click", function() {
            const dataset = this.dataset;
            const mode = dataset.mode;
            const targetName = dataset.targetName;
            const target = document[targetName];
            if (!target) return;

            target.mode.value = mode;
            if (mode == 'delete' && !confirm('정말 삭제하시겠습니까?')) {
                return;
            }

            target.submit();
        });
    }
    /** 양식 공통 처리 E */

    /** 전체 선택 처리 S */
    const checkalls = document.getElementsByClassName("checkall");
    for (const el of checkalls) {
        el.addEventListener("click", function() {
            const targetName = this.dataset.targetName;
            if (!targetName) return;

            const chkEls = document.getElementsByName(targetName);
            for (const chk of chkEls) {
                chk.checked = this.checked;
            }
        });
    }
    /** 전체 선택 처리 E */
});