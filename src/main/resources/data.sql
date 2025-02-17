INSERT INTO address (id, city, created_date, modified_date, is_deleted)
VALUES (1, '서울', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO address (id, city, created_date, modified_date, is_deleted)
VALUES (2, '경기', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO address (id, city, created_date, modified_date, is_deleted)
VALUES (3, '부산', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO address (id, city, created_date, modified_date, is_deleted)
VALUES (4, '대전', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PetGroup 데이터 추가
INSERT INTO petgroup (id, groupname, code, created_date, modified_date, is_deleted)
VALUES (1, '강아지', 'DOG', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petgroup (id, groupname, code, created_date, modified_date, is_deleted)
VALUES (2, '고양이', 'CAT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PetGroupType 데이터 추가
INSERT INTO petgrouptype (id, typename, code, petgroup_id, created_date, modified_date, is_deleted)
VALUES (1, '말티즈', 'MALTESE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petgrouptype (id, typename, code, petgroup_id, created_date, modified_date, is_deleted)
VALUES (2, '푸들', 'POODLE', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petgrouptype (id, typename, code, petgroup_id, created_date, modified_date, is_deleted)
VALUES (3, '페르시안', 'PERSIAN', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petgrouptype (id, typename, code, petgroup_id, created_date, modified_date, is_deleted)
VALUES (4, '러시안블루', 'RUSSIAN_BLUE', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PetSize 데이터 추가
INSERT INTO petsize (id, size_info, created_date, modified_date, is_deleted)
VALUES (1, '소형(10kg 이하)', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petsize (id, size_info, created_date, modified_date, is_deleted)
VALUES (2, '중형(20kg 이하)', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petsize (id, size_info, created_date, modified_date, is_deleted)
VALUES (3, '대형(30kg 이상)', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PetService 데이터 추가
INSERT INTO petservice (petservice_id, servicename, created_date, modified_date, is_deleted)
VALUES (1, '돌봄', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petservice (petservice_id, servicename, created_date, modified_date, is_deleted)
VALUES (2, '훈련', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petservice (petservice_id, servicename, created_date, modified_date, is_deleted)
VALUES (3, '산책', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
