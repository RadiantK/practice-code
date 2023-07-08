let detail = {

	init: function() {
		detail.eventInit();

		detail.getNotice();
	},

	eventInit: function() {
		const $evo = document.querySelectorAll("[data-act]");

		$evo.forEach(e => {
			e.addEventListener('click', detail.action);
		})
	},

	action: function(e) {
		const eType = e.type;

		const eTarget = e.currentTarget;

		const actValue = eTarget.getAttribute('data-act');

		if (eType ==='click') {
			if (actValue === 'toList') {
				detail.listPage();
			} else if (actValue === 'editPage') {
				detail.editPage();
			} else if (actValue === 'removeNotice') {
				detail.removeNotice();
			}
		}
	},

	// 회원 정보
	getMemberInfo: function() {
		const memberEl = document.querySelector('.comment-write');
		const loginMember = memberEl.dataset['id'];

		return loginMember;
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

	// 게시글 상세정보 api
	getNotice: async function() {

		const url = "/api/notices/detail";
		const param = {
			noticeId: detail.getNoticeNum()
		};

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

		detail.printNotice(response);
	},

	// api로 얻어온 게시글 출력
	printNotice: function(resp) {
		// api 데이터 얻기
		const notice = resp.data;

		// 게시물 상세 내용을 화면에 랜더링하기 위해 DOM 얻기
		const noticeEl = document.querySelector('.notice');
		let detailEl = noticeEl.querySelector('.detail');
		let attachEl = noticeEl.querySelector('.attach');

		// 게시물 내용 이스케이프 처리
		let map = {
			'&': '&amp;',
			'<': '&lt;',
			'>': '&gt;',
			'"': '&quot;',
			"'": '&#039;'
		};

		let changeContent = notice.content.replace(/[&<>"']/g, function(m) { return map[m]; });
		changeContent = changeContent.replace(/\n/gi, '<br/>');

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

			const pEl = document.createElement('p');
			pEl.classList.add('lead');
			pEl.style.fontSize = ".9rem";

			if (n == "content") {
				pEl.innerHTML = changeContent;
			} else {
				pEl.innerHTML = notice[n];
			}

			detailEl.appendChild(labelEl);
			detailEl.appendChild(pEl);
		}

		attachEl.innerHTML = '<label class="label">첨부파일</label>';
		for (let i = 0; i < notice.attachFiles.length; i++) {
			let file = notice.attachFiles[i];

			let aEl = document.createElement('a');
			aEl.href = "/attach/download/" + file.attachFileId;

			let pEl = document.createElement('p');
			pEl.classList.add('lead');

			let imgEl = document.createElement('img');
			imgEl.classList.add('imgFile');
			imgEl.src = "/attach/images/" + file.attachFileId;

			pEl.appendChild(imgEl);
			aEl.appendChild(pEl);
			attachEl.appendChild(aEl);
		}

		const editBtnEl = document.querySelector('.edit');
		const removeBtnEl = document.querySelector('.remove');

		const currMemberId = detail.getMemberInfo();

		if (parseInt(currMemberId) !== notice.memberId) {
			editBtnEl.style.display = "none";
			removeBtnEl.style.display = "none";
		}
	},

	// 게시글 삭제
	removeNotice: async function() {
		// 회원 아이디 추출
		const memberId = detail.getMemberInfo();

		if (memberId === null || memberId === undefined) {
			alert('게시글은 로그인 후 제거할 수 있습니다.');
			return;
		}

		const param = {
			noticeId: detail.getNoticeNum(),
			memberId: memberId
		}
		const url = "/api/notices/remove";

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

		if (response.data === "ok") {
			alert("게시글이 제거되었습니다.");
			location.href = "/notice/list"
			return;
		} else {
			alert("자신의 게시글이 아니면 제거할 수 없습니다.");
			return;
		}
	},

	// 목록 페이지로 이동
	listPage: function() {
		location.href = "/notice/list";
	},

	// 게시글 수정 페이지로 이동
	editPage: function() {
		location.href = "/notice/" + comment.getNoticeNum() + "/edit";
	},
};


const comment = {

	init() {
		comment.eventInit();

		comment.commentList();
		comment.commentInputInit();
	},

	eventInit() {
		const $evo = document.querySelectorAll('[data-act]');
		console.log($evo);

		$evo.forEach(e => {
			e.addEventListener('click', comment.action);
		})
	},

	action(e) {
		const eType = e.type;

		const eTarget = e.currentTarget;

		const actValue = eTarget.getAttribute('data-act');

		if (eType ==='click') {
			if (actValue === 'comment-write') {
				comment.addComment();
			} else if (actValue === 'comment-event') {
				comment.commentEvent(e);
			} else if (actValue === 'toList') {
				comment.listPage();
			} else if (actValue === 'editPage') {
				comment.editPage();
			} else if (actValue === 'comment-edit-save') {
				comment.editComment();
			} else if(actValue === 'modal-close') {
				comment.modalClose();
			}
		}
	},

	// 회원 정보
	getMemberInfo: function() {
		const memberEl = document.querySelector('.comment-write');
		const loginMember = memberEl.dataset['id'];

		return loginMember;
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

	// 로그인 한 회원일 때 disabled
	commentInputInit: function() {
		const loginMember = comment.getMemberInfo();
		const commentInputEl = document.querySelector('#comment-input');
		const addCommentEl = document.querySelector('.addComment');

		// 댓글 등록 차단
		if (loginMember === null || loginMember === undefined) {
			commentInputEl.value = '로그인 후 이용 가능합니다.';
			commentInputEl.disabled = true;
			addCommentEl.disabled = true;
		}
	},

	// 댓글 목록 출력
	printComments: function(resp) {

		// 회원 정보
		const loginMember = comment.getMemberInfo();

		//댓글 목록을 삽입할 태그
		const commentsEl = document.querySelector('.comments');

		// 댓글 리스트 
		const list = resp.data;
		console.log(list);

		commentsEl.innerHTML = ""; // 댓글 초기화

		const commTitleEl = document.createElement('div');
		commTitleEl.classList.add('card-header');
		commTitleEl.innerText = "댓글";
		commentsEl.appendChild(commTitleEl);

		const commListEl = document.createElement('div');
		commListEl.classList.add('comment-list');

		let map = {
			'&': '&amp;',
			'<': '&lt;',
			'>': '&gt;',
			'"': '&quot;',
			"'": '&#039;'
		};

		for (let i = 0; i < list.length; i++) {
			let comm = list[i];

			const divEl = document.createElement('div');
			divEl.setAttribute('class', 'card-body comment')

			// 이스케이프 처리
			let changeContent = comm.content.replace(/[&<>"']/g, function(m) { return map[m]; });
			changeContent.replace(/\n/gi, '<br/>');

			const pEl = document.createElement('p');
			pEl.classList.add('card-text');
			pEl.innerText = changeContent;
			divEl.appendChild(pEl);

			if (comm.memberId === parseInt(loginMember)) {

				const editBtnEl = document.createElement('button');
				editBtnEl.setAttribute('class', 'btn btn-primary');
				editBtnEl.setAttribute('data-value', comm.commentId);
				editBtnEl.setAttribute('data-comment', changeContent);
				editBtnEl.setAttribute('data-act', "comment-edit");
				editBtnEl.setAttribute('type', 'button');
				editBtnEl.style.marginRight = "8px";
				editBtnEl.innerText = "수정하기";

				const removeBtnEl = document.createElement('button');
				removeBtnEl.setAttribute('class', 'btn btn-primary');
				removeBtnEl.setAttribute('data-value', comm.commentId);
				removeBtnEl.setAttribute('data-act', "comment-remove");
				removeBtnEl.setAttribute('type', 'button');
				removeBtnEl.innerText = "삭제하기";

				divEl.appendChild(editBtnEl);
				divEl.appendChild(removeBtnEl);
			}

			commListEl.appendChild(divEl);
			commentsEl.appendChild(commListEl);

		}
	},

	// 댓글 등록 결과 출력
	printAddComment: function (response){

		// 댓글 등록 실패
		if (response.data !== 'ok') {
			alert('댓글 등록에 실패했습니다.');
			return;
		}

		// 댓글 등록 성공. => 댓글 초기화
		const commentInputEl = document.querySelector('#comment-input');
		alert('댓글이 등록되었습니다.');
		commentInputEl.value = "";
		comment.commentList(comment.getNoticeNum());
	},

	// 댓글 이벤트 처리
	commentEvent: function(e) {
		e.stopPropagation();

		const actValue = e.target.getAttribute('data-act')
		const commentId = e.target.getAttribute('data-value');
		const content = e.target.getAttribute('data-comment');

		if (actValue == "comment-remove") {
			comment.removeComment(commentId);
		} else if (actValue == "comment-edit") {
			comment.editSet(commentId, content);
		}
	},

	editSet: function(commentId, content) {
		$('#myModal').modal('show');

		document.querySelector('.comment-edit-id').value = commentId;
		document.querySelector('.comment-edit').innerHTML = content;
	},

	// 모달 창 닫기
	modalClose: function() {
		document.querySelector('.comment-edit-id').value = "";
		document.querySelector('.comment-edit').innerHTML = "";

		$('#myModal').modal('hide');
	},

	editComment: async function() {
		const commentId = document.querySelector('.comment-edit-id').value;
		const content = document.querySelector('.comment-edit').value;
		const memberId = comment.getMemberInfo();

		const param = {
			commentId: commentId,
			content: content,
			memberId: memberId
		}

		const url = "/api/comments/edit";

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

		if (response.data != "ok") {
			alert("댓글 수정에 실패했습니다");
			return;
		}

		comment.modalClose();
		comment.commentList();
		alert("댓글이 수정되었습니다.");
	},

	// 댓글 등록
	addComment: async function() {

		const commentInputEl = document.querySelector('#comment-input');

		if (commentInputEl.value == "") {
			alert("댓글을 입력하세요.");
			return;
		}

		const param = {
			content: commentInputEl.value,
			noticeId: comment.getNoticeNum(),
			memberId: comment.getMemberInfo()
		}
		const url = "/api/comments/regist";

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


		comment.printAddComment(response);
	},

	// 댓글목록 조회
	commentList: async function() {

		const url = "/api/comments";
		const param = {
			noticeId : comment.getNoticeNum()
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


		comment.printComments(response);
	},

	// 댓글 삭제
	removeComment: async function(commentId) {

		const url = "/api/comments/remove";
		const param = {
			commentId: commentId
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

		comment.printRemoveComment(response);
	},

	// 댓글 삭제 메시지 출력
	printRemoveComment: function(response) {
		const data = response.data;
		const noticeId = comment.getNoticeNum();

		if (data !== 'ok') {
			alert('댓글 제거에 실패했습니다.');
			return;
		}

		alert('댓글이 제거되었습니다.');
		comment.commentList(noticeId);
	},
};

detail.init();
comment.init();
