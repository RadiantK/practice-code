CREATE TABLE IF NOT EXISTS `board`.`attach_file` (
  `attach_file_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `org_file_name` VARCHAR(255) NULL DEFAULT NULL,
  `save_file_name` VARCHAR(255) NULL DEFAULT NULL,
  `ext` VARCHAR(45) NULL DEFAULT NULL,
  `size` BIGINT(20) NULL DEFAULT NULL,
  `notice_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`attach_file_id`),
  INDEX `fk_attach_file_notice1_idx` (`notice_id` ASC) VISIBLE,
  CONSTRAINT `fk_attach_file_notice1`
    FOREIGN KEY (`notice_id`)
    REFERENCES `board`.`notice` (`notice_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


CREATE TABLE IF NOT EXISTS `board`.`member` (
  `member_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `login_id` VARCHAR(45) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `last_login_date` BIGINT(20) NOT NULL,
  PRIMARY KEY (`member_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3
COMMENT = '회원 테이블';

CREATE TABLE IF NOT EXISTS `board`.`comment` (
  `comment_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `content` VARCHAR(300) NULL DEFAULT NULL,
  `notice_id` BIGINT(20) NOT NULL,
  `member_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`comment_id`),
  INDEX `fk_comment_notice1_idx` (`notice_id` ASC) VISIBLE,
  INDEX `fk_comment_member1_idx` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_comment_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `board`.`member` (`member_id`),
  CONSTRAINT `fk_comment_notice1`
    FOREIGN KEY (`notice_id`)
    REFERENCES `board`.`notice` (`notice_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


CREATE TABLE IF NOT EXISTS `board`.`notice` (
  `notice_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NULL DEFAULT NULL,
  `content` VARCHAR(400) NULL DEFAULT NULL,
  `created_at` BIGINT(20) NULL DEFAULT NULL,
  `updated_at` BIGINT(20) NULL DEFAULT NULL,
  `member_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`notice_id`),
  INDEX `fk_notice_member_idx` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_notice_member`
    FOREIGN KEY (`member_id`)
    REFERENCES `board`.`member` (`member_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;



DELIMITER $$
CREATE PROCEDURE save_notice_pro (
	IN title VARCHAR(100),
    IN content VARCHAR(300),
    IN member_id BIGINT
)
BEGIN
	INSERT INTO
		notice (
			title,
			content,
            created_at,
            updated_at,
            member_id
		)
        VALUES (
			title,
            content,
            now(),
            now(),
            member_id
        );
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE save_attach_file_pro (
	IN org_file_name VARCHAR(200),
    IN save_file_name VARCHAR(300),
    IN ext VARCHAR(40),
    IN size VARCHAR(40),
    IN notice_id BIGINT
)
BEGIN
	INSERT INTO 
		attach_file (
			org_file_name, 
			save_file_name, 
			ext,
			size, 
			notice_id
		)
		VALUES (
			org_file_name,
            save_file_name,
            ext,
            size,
            notice_id
        );
END $$
DELIMITER ;

-- 첨부파일 제거
DELIMITER $$
create procedure attach_file_remove_by_notice_id (
	IN id BIGINT
)
BEGIN
	delete from
		attach_file
	where notice_id = id;
END $$
DELEMITER ;

-- 댓글 제거
DELIMITER $$
create procedure comment_remove_by_notice_id (
	IN id BIGINT
)
BEGIN
	delete from
		comment
	where notice_id = id;
END $$
DELEMITER ;

-- 게시글 제거
DELIMITER $$
create procedure notice_remove_by_notice_id (
	IN id BIGINT
)
begin
	DELETE FROM
		notice
	where notice_id = id;
END $$
delimiter ;

DELIMITER $$
create procedure removeAll_by_notice_id (
	IN id BIGINT
)
begin
	begin
		declare exit handler for SQLEXCEPTION
			rollback;
		end;
	call attach_file_remove_by_notice_id(id);
    call comment_remove_by_notice_id(id);
	call notice_remove_by_notice_id(id);
end $$
DELIMITER ;


insert into member (login_id, password, last_login_date) values ('test', 'test', now());