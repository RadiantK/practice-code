let notice = {
	
	init: function() {
		notice.getNoticeList(1);
		
		notice.eventInit();
	},
	
	eventInit: function() {
		const $evo = document.querySelectorAll('[data-act]');
		
		$evo.forEach(e => {
			e.addEventListener('click', notice.action);
			e.addEventListener('keyup', notice.action);
		})
	},
	
	action: function(e) {
		const eType = e.type;
		
		const eTarget = e.currentTarget;
		
		const actValue = eTarget.getAttribute('data-act');
		
		if (eType === 'click') {
			if (actValue === 'clickSearch') {
				notice.getNoticeList(1);
				return;
			} else if (actValue == 'regist') {
				notice.registPage();
			} else if (actValue == 'home') {
				notice.homePage();
			}
		} else if (eType === 'keyup') {
			if (actValue === 'inputSearch') {
				if (e.keyCode == 13) {
					notice.getNoticeList(1);
					return;
				}
			}
		}
	},
	
	// 게시글 목록
	getNoticeList: async function(p = 1) {
		
		const url = "/api/notices";
		
		const typeEl = document.querySelector('.type').value;
		const wordEl = document.querySelector('.word').value;
		
		const param = {
			page: p,
			type: typeEl,
			word: wordEl
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
		
		notice.printNoticeList(response);
	},
	
	printNoticeList: function(response) {
		
		const list = response.data; // 전체 리스트 목
		const listInfo = response.listInfo; // 페이지, 검색파라미터
		const totalCount = response.totalCount; // 총 게시물 수
		
		/**
		 * 게시판 처리
		 */
		const tableEl = document.querySelector('.table');
		
		tableEl.innerHTML = ""; // 게시물 초기화
		
		const theadEl = document.createElement('thead');
		const theadTrEl = document.createElement('tr');
		
		theadTrEl.innerHTML = 
	        '    <th scope="col">게시글 번호</th>' + 
	        '    <th scope="col">제목</th>' + 
	        '    <th scope="col">게시글 내용</th>' +
	        '    <th scope="col">등록일</th>';
	    
	    theadEl.appendChild(theadTrEl);
	    tableEl.appendChild(theadEl);
	    
	    let map = {
		    '&': '&amp;',
		    '<': '&lt;',
		    '>': '&gt;',
		    '"': '&quot;',
		    "'": '&#039;'
	    };
	    
	    // table body에 데이터 삽입
	    const tbodyEl = document.createElement('tbody');
	    for (let i = 0; i < list.length; i++) {
			let trEl = document.createElement('tr');
			let currList = list[i];

			// 객체 요소 for문 			
			for (n in currList) {
				let tdEl = document.createElement('td');
				
				// 내용일 때 상세 페이지로 갈 수 있도록 링크 설정
				if (n == 'content') {
					let aEl = document.createElement('a');
					aEl.href = href="/notice/" + currList.id + "/detail"
					
					let changeContent = currList[n].replace(/[&<>"']/g, function(m) { return map[m]; });
					aEl.innerHTML = changeContent;
					
					tdEl.appendChild(aEl);
				} else {
					tdEl.innerText = currList[n];	
				}
				
				trEl.appendChild(tdEl);
			}
			
		    tbodyEl.appendChild(trEl);
		}
		tableEl.appendChild(tbodyEl);
		
		/**
		 * 페이징
		 */
		const pagingEl = document.querySelector('.paging'); // 페이지 목록을 담을 변수
		const currPage = listInfo.page; // 현재 페이지
		const currType = listInfo.type === "" ? "" : listInfo.type; // 검색타입
		const currWord = listInfo.word === "" ? "" : listInfo.word; // 검색어

		let pageCount = 5; // 출력할 페이지 수
		let endPage = parseInt(Math.ceil(currPage / 5.0)) * pageCount; // 마지막페이지
		let startPage = endPage - (pageCount - 1); // 시작 페이지
		let totalPage = parseInt(Math.ceil(totalCount / 5.0)); // 전체 페이지
		if (endPage > totalPage) {
			endPage = totalPage;
		}
		
		// 검색 값
		document.querySelector('.type').value = currType;
		document.querySelector('.word').value = currWord;
		
		// 페이징 초기화
		pagingEl.innerHTML = "";
		
		
		// 이전 페이지
		let $prevLi = document.createElement('li');
		$prevLi.classList.add('page-item');
		let $prevA = document.createElement('a');
		$prevA.setAttribute('class', 'page-link move');
		let $prevI = document.createElement('i');
		$prevI.setAttribute('class', 'bi bi-chevron-left');
		
		// 이전 페이지
		if (currPage <= 1) {
		    $prevA.href = "javascript:notice.getNoticeList(" + (1) + `, '${currType}', '${currWord}'` +")"
		} else {
		    $prevA.href = "javascript:notice.getNoticeList(" + (currPage - 1) + `, '${currType}', '${currWord}'` +")"
		}
		
		$prevA.appendChild($prevI);
		$prevLi.appendChild($prevA);
		pagingEl.appendChild($prevLi);
	
		// 페이지 1~5 6~10
	    for (let i = startPage; i <= endPage; i++) {
			let $pageNumlistLi = document.createElement('li');
			let $pageNumListA = document.createElement('a');
			$pageNumListA.classList.add('page-link');
			$pageNumListA.href = "javascript:notice.getNoticeList(" + (i) + `, '${currType}', '${currWord}'` +")";
			$pageNumListA.innerText = i;
						
			if (i === currPage) {
				$pageNumlistLi.setAttribute('class', 'page-item active');
			} else {
			}
			
			$pageNumlistLi.appendChild($pageNumListA);
			pagingEl.appendChild($pageNumlistLi);
		}
		
		// 다음 페이지
		let $nextLi = document.createElement('li');
		$nextLi.classList.add('page-item');
		let $nextA = document.createElement('a');
		$nextA.setAttribute('class', 'page-link move');
		let $nextI = document.createElement('i');
		$nextI.setAttribute('class', 'bi bi-chevron-right');
		
		if (currPage >= totalPage) {
		    $nextA.href = "javascript:notice.getNoticeList(" + (currPage) + `, '${currType}', '${currWord}'` +")"
		} else {
		    $nextA.href = "javascript:notice.getNoticeList(" + (currPage + 1) + `, '${currType}', '${currWord}'` +")"
		}
		
		$nextA.appendChild($nextI);
		$nextLi.appendChild($nextA);
		pagingEl.appendChild($nextLi);
	},
	
	registPage: function() {
		location.href = '/notice/regist';
	},
	
	homePage: function() {
		location.href = '/';
	}
};

notice.init();
