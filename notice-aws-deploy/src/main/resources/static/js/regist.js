let noticeService = {
	
	init: function() {
		noticeService.eventInit();
	},
	
	eventInit: function() {
		const $act = document.querySelectorAll('[data-act]');
		
		$act.forEach(e => {
			e.addEventListener('click', noticeService.action);
			e.addEventListener('change', noticeService.action);
			e.addEventListener('dragleave', noticeService.action);
			e.addEventListener('dragover', noticeService.action);
			e.addEventListener('drop', noticeService.action);
		});
	},
	
	action: function(e) {
			
		const eType = e.type; // 이벤트 타입
		
		const eTarget = e.currentTarget; // 이벤트가 작동한 타겟
		
		const actValue = eTarget.getAttribute('data-act'); // data-act 값
		
		if (eType === 'click') {
			if (actValue === "regist") {
				noticeService.registCheck();
			} else if (actValue === "cancel") {
				noticeService.prevPage();
			}
		} else if (eType === 'change') {
			noticeService.showFiles(e.target.files);
		} else if (eType === 'dragleave') {
			noticeService.dragleaveEvent(e);
		} else if (eType === 'dragover') {
			noticeService.dragoverEvent(e);
		} else if (eType === 'drop') {
			noticeService.dropEvent(e);
		}
		
	},
	
	// 파일 출력
	showFiles: function(files) {
		
		let dropZone = document.querySelector(".drop-zone")
		
        dropZone.innerHTML = ""
        for(let i = 0, len = files.length; i < len; i++) {
            dropZone.innerHTML += "<div>" + files[i].name + "</div>"
        }
	},
	
	// 드래그 앤 드롭 css처리
	toggleClass: function(className) {
		
		let dropZone = document.querySelector(".drop-zone");
		let list = ["dragleave", "dragover", "drop"];

        for (let i = 0; i < list.length; i++) {
            if (className === list[i]) {
                dropZone.classList.add("drop-zone-" + list[i]);
            } else {
                dropZone.classList.remove("drop-zone-" + list[i]);
            }
        }
	},
	
	// 파일 선택 시
	selectFile: function(files) {
		
		let $file = document.getElementById("attachFiles");
		
        // input file 영역에 드랍된 파일들로 대체
        $file.files = files;
        noticeService.showFiles($file.files);
    },
	
	// 드래그 영역에서 떠났을 때
	dragleaveEvent: function(e) {
		e.stopPropagation();
        e.preventDefault();

        noticeService.toggleClass("dragleave");
	},
	
	// 드래그 영역으로 들어왔을 때
	dragoverEvent: function(e) {
		e.stopPropagation();
        e.preventDefault();

        noticeService.toggleClass("dragover");
	},
	
	// 드래그 영역으로 파일이 드랍됐을 때
	dropEvent: function(e) {
		e.preventDefault();

        noticeService.toggleClass("drop");

        let files = e.dataTransfer && e.dataTransfer.files;

        if (files != null) {
            if (files.length < 1) {
                alert("폴더 업로드 불가");
                return;
            }
            noticeService.selectFile(files);
        } else {
            alert("error!");
        }
	},
	
	registCheck: function() {
		
		// 폼 태그
		const formEl = document.querySelector('.regist-form');
		// 게시물 생성 정보
		const titleEl = formEl.querySelector('#title');
		const contentEl = formEl.querySelector('#content');
		const attachFilesEl = formEl.querySelector('#attachFiles');
		
		// 게시글 제목 제목 체크
		if (titleEl.value === "" || titleEl.value === undefined) {
			alert('제목을 입력하세요.');
			titleEl.focus();
			return;
		}
		
		// 게시글 내용 체크
		if (contentEl.value === "" || contentEl.value === undefined) {
			alert('내용을 입력하세요.');
			contentEl.focus();
			return;
		}
		
		// formData에 데이터 담기
		let formData = new FormData();
		formData.append('title', titleEl.value);
		formData.append('content', contentEl.value);
		
		// 첨부파일 추출
		const files = attachFilesEl.files;
		for (let i = 0; i < files.length; i++) {
			let currFile = files[i];
			
			// 확장자 추출
			let idx = currFile.name.lastIndexOf('.');
			let ext = currFile.name.substring(idx+1, currFile.name.length);
			
			let check = noticeService.checkExtension(ext);
			if (!check) {
				alert('첨부파일의 확장자는 png, jpg, jpeg 만 업로드 할 수 있습니다.')
				return;
			}
			
			formData.append('attachFiles', currFile);
		}
		
		const url = "/api/notices/new";
		
		noticeService.regist(url, formData);
		
	},
	
	// 확장자 검사
	checkExtension: function (ext) {
		const checkExt = ['jpg', 'jpeg', 'png']
		for (let j = 0; j < checkExt.length; j++) {
			if (ext === checkExt[j]) {
				return true;
			}
		}
		return false;
	},
	
	// 이전 페이지로 이동
	prevPage: function() {
		history.back();	
	},
	
	// 게시글 등록
	regist: async function (url, param) {
		
		const response = await fetch(url, {
			method: 'post',
			headers: {
				
			},
			body: param
		})
		.then(resp => resp.json())
		.then(data => data)
		.catch(error => alert(error));
		
		if (response.data !== 'ok') {
			alert('게시글 등록에 실패했습니다. 글 정보를 다시 확인하세요.');
			return;
		}
		
		location.href = '/notice/list';
	}
}

noticeService.init();
