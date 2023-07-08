let notice = {
	
	init: function() {
		notice.getNotice();
		
		notice.eventInit();
	},
	
	eventInit: function() {
		const $evo = document.querySelectorAll('[data-act]');
		
		$evo.forEach(e => {
			e.addEventListener('click', notice.action);
		});
	},
	
	action: function(e) {
		
		const eType = e.type;
		
		const eTarget = e.currentTarget;
		
		const actValue = eTarget.getAttribute('data-act');
		
		if (eType === 'click') {
			if (actValue === "edit") {
				notice.editNotice();
			} else if (actValue === "cancel") {
				notice.detailPage();
			}
		}
	},
	
	// 게시물 번호 얻기
	getNoticeNum: function() {
		let url = location.href;
		let pos = url.lastIndexOf('/');
		let extNoticeNum = url.substring(0, pos);
		let nextPos = extNoticeNum.lastIndexOf('/');
		let noticeNum = extNoticeNum.substring(nextPos+1, extNoticeNum.length);
		
		return noticeNum;
	},
	
	// 파일 확장자 체크
	checkExtension: function (ext) {
		const checkExt = ['jpg', 'jpeg', 'png']
		for (let j = 0; j < checkExt.length; j++) {
			if (ext === checkExt[j]) {
				return true;
			}
		}
		return false;
	},
	
	// 게시물 상세보기
	detailNotice: async function(response) {
		
		// api 데이터 얻기
		const notice = response.data;
		console.log(notice);
		
		// 게시물 상세 내용을 화면에 랜더링하기 위해 DOM 얻기
		const noticeEl = document.querySelector('.notice');
		let detailEl = noticeEl.querySelector('.detail');
		let removeEl = noticeEl.querySelector('.remove');
	
		const tempObj = {
			noticeId: "게시판 번호",
			title: "게시판 제목",
			content: "게시판 내용",
			createdAt: "게시판 등록일",
			updatedAt: "게시판 수정일",
		}
		
		// 동적인 화면 출력
		for (n in notice) {
			if (n == "memberId" || n == "attachFiles") continue;
			
			const labelEl = document.createElement('label');
			labelEl.classList.add('label');
			labelEl.innerHTML = tempObj[n];
			detailEl.appendChild(labelEl);
			
			if (n == "content") {
				let inputEl = document.createElement('textarea');
				inputEl.setAttribute('class', 'form-control mb-3');
				inputEl.setAttribute('id', 'contents');
				inputEl.innerHTML = notice[n];
				
				detailEl.appendChild(inputEl);
			} else {
				let inputEl = document.createElement('input');
				inputEl.setAttribute('class', 'form-control mb-3');
				inputEl.setAttribute('id', n);
				inputEl.setAttribute('value', notice[n]);
				
				if (n != "title") {
					inputEl.readOnly = true;
				} 
				
				detailEl.appendChild(inputEl);
			}
			
		}
		
		/* 동적인 화면 출력
		detailEl.innerHTML = 
			'<label class="label">게시판 번호</label>' +
	      	'<input type="text"  class="form-control mb-3" id="noticeId" readonly value="' + notice.noticeId + '"/>' +
			'<label class="label">게시판 제목</label>' +
	      	'<input type="text"  class="form-control mb-3" id="title" value="' + notice.title + '"/>' +
			'<label class="label">게시판 내용</label>' +
	      	'<textarea class="form-control mb-3" id="contents">' + notice.content + '</textarea>' +
			'<label class="label">게시글 등록일</label>' +
	        '<input type="text"  class="form-control mb-3" readonly value="' + notice.createdAt + '"/>' +
			'<label class="label">게시글 수정일</label>' +
		    '<input type="text"  class="form-control mb-3" readonly value="' + notice.updatedAt + '"/>';*/
		
		// 첨부파일 출력
		const labelEl = document.createElement('label');
		labelEl.classList.add('label');
		labelEl.innerHTML = '삭제하기';
		removeEl.appendChild(labelEl);
		
		let orgFiles = notice.attachFiles;
		for (let i = 0; i < orgFiles.length; i++) {
			let orgFile = orgFiles[i];
			
			const divEl = document.createElement('div');
			divEl.classList.add('form-check');
			divEl.classList.add('files');

			const checkBoxEl = document.createElement('input');
			checkBoxEl.setAttribute('class', 'form-check-input selectAttach');
			checkBoxEl.setAttribute('type', 'checkbox');
			checkBoxEl.value = orgFile.attachFileId;
			checkBoxEl.setAttribute('id', "defaultCheck" + i)
			
			const labelEl = document.createElement('label');
			labelEl.setAttribute('class', 'form-check-label');
			labelEl.setAttribute('for', "defaultCheck" + i);
			labelEl.innerText = orgFile.orgFileName;
			
			divEl.appendChild(checkBoxEl);
			divEl.appendChild(labelEl);
			removeEl.appendChild(divEl);
		}
	},

	// 게시글 상세 페이지로 이동
	detailPage: function() {
		location.href = "/notice/" + notice.getNoticeNum() + "/detail";
	},
	
	// 게시글 파라미터 체크
	editNoticeCheck: function() {
		// 요소들을 얻어오기
		const editFormEl = document.querySelector('.edit-form');
		const titleEl = editFormEl.querySelector('#title');
		const contentEl = editFormEl.querySelector('#contents');
		const attachFilesEl = editFormEl.querySelector('#attachFiles');
		const selectAttachEl = editFormEl.querySelectorAll('.selectAttach');
		console.log(contentEl);
		// 게시글 제목 제목 체크
		if (titleEl.value === "" || titleEl.value === undefined) {
			alert('제목을 입력하세요.');
			titleEl.focus();
			return false;
		}
		
		// 게시글 내용 체크
		if (contentEl.value === "" || contentEl.value === undefined) {
			alert('내용을 입력하세요.');
			contentEl.focus();
			return false;
		}
		
		// 폼 데이터에 데이터들 set
		let formData = new FormData();
		formData.append('noticeId', notice.getNoticeNum())
		formData.append('title', titleEl.value)
		formData.append('content', contentEl.value)
		
		let files = attachFilesEl.files;
		for (let i = 0; i < files.length; i++) {
			let currFile = files[i];
		
			// 확장자 추출
			let idx = currFile.name.lastIndexOf('.');
			let ext = currFile.name.substring(idx+1, currFile.name.length);
			
			let check = notice.checkExtension(ext);
			if (!check) {
				alert('첨부파일의 확장자는 png, jpg, jpeg 만 업로드 할 수 있습니다.')
				return false;
			}
		
			formData.append('attachFiles', currFile);
		}
		
		for (let i = 0; i < selectAttachEl.length; i++) {
			if (selectAttachEl[i].checked) {
				formData.append('removeAttachId', selectAttachEl[i].value);	
			}
		}
		
		return formData;
	},
	
	// 게시글 조회
	getNotice: async function() {
		
		const url = "/api/notices/detail";
		// api
		const param = {
			noticeId: notice.getNoticeNum()
		}
		
		const response = await fetch(url, {
			method: 'post',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(param)
		})
		.then(resp => resp.json())
		.then(data => data)
		.catch(error => alert(error));
		
		notice.detailNotice(response);
	},
	
	// 게시글 수정
	editNotice: async function() {
		
		const url = "/api/notices/edit";
		const param = notice.editNoticeCheck();
		
		if (!param) {
			alert("입력이 올바르지 않습니다.");
			return;
		}
		
		const response = await fetch(url, {
			method: 'post',
			headers: {
				
			},
			body: param
		})
		.then(resp => resp.json())
		.then(data => data)
		.catch(error => alert(error));
		
		const data = response.data;
		
		if (data !== 'ok') {
			alert('게시글 수정에 실패했습니다.');
			return;	
		}
		
		location.href = 'http://localhost:8080/notice/' + notice.getNoticeNum() + '/detail';
	}

};

notice.init();
