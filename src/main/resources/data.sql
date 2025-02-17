-- 기존 데이터 삽입 전에 시퀀스 초기화
ALTER SEQUENCE petgroup_id_seq RESTART WITH 1000;
ALTER SEQUENCE petgrouptype_id_seq RESTART WITH 1000;
ALTER SEQUENCE petsize_id_seq RESTART WITH 1000;
ALTER SEQUENCE petservice_petservice_id_seq RESTART WITH 1000;
ALTER SEQUENCE user_seq RESTART WITH 1000;
ALTER SEQUENCE petsitter_id_seq RESTART WITH 1000;
ALTER SEQUENCE petsitter_petservice_id_seq RESTART WITH 1000;
ALTER SEQUENCE petcaretime_id_seq RESTART WITH 1000;
ALTER SEQUENCE pet_id_seq RESTART WITH 1000;
ALTER SEQUENCE petsitter_reserve_id_seq RESTART WITH 1000;
ALTER SEQUENCE paytype_id_seq RESTART WITH 1000;

-- Address 데이터 추가
INSERT INTO address (id, city, created_date, modified_date, is_deleted)
VALUES (1001, '서울', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO address (id, city, created_date, modified_date, is_deleted)
VALUES (1002, '경기', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO address (id, city, created_date, modified_date, is_deleted)
VALUES (1003, '부산', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO address (id, city, created_date, modified_date, is_deleted)
VALUES (1004, '대전', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PetGroup 데이터 추가
INSERT INTO petgroup (id, groupname, code, created_date, modified_date, is_deleted)
VALUES (1001, '강아지', 'DOG', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petgroup (id, groupname, code, created_date, modified_date, is_deleted)
VALUES (1002, '고양이', 'CAT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PetGroupType 데이터 추가
INSERT INTO petgrouptype (id, typename, code, petgroup_id, created_date, modified_date, is_deleted)
VALUES (1001, '말티즈', 'MALTESE', 1001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petgrouptype (id, typename, code, petgroup_id, created_date, modified_date, is_deleted)
VALUES (1002, '푸들', 'POODLE', 1001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petgrouptype (id, typename, code, petgroup_id, created_date, modified_date, is_deleted)
VALUES (1003, '페르시안', 'PERSIAN', 1002, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petgrouptype (id, typename, code, petgroup_id, created_date, modified_date, is_deleted)
VALUES (1004, '러시안블루', 'RUSSIAN_BLUE', 1002, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PetSize 데이터 추가
INSERT INTO petsize (id, size_info, created_date, modified_date, is_deleted)
VALUES (1001, '소형(10kg 이하)', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petsize (id, size_info, created_date, modified_date, is_deleted)
VALUES (1002, '중형(10kg~25kg 이하)', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petsize (id, size_info, created_date, modified_date, is_deleted)
VALUES (1003, '대형(25kg 초과)', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PetService 데이터 추가
INSERT INTO petservice (petservice_id, servicename, created_date, modified_date, is_deleted)
VALUES (1001, '돌봄', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petservice (petservice_id, servicename, created_date, modified_date, is_deleted)
VALUES (1002, '훈련', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petservice (petservice_id, servicename, created_date, modified_date, is_deleted)
VALUES (1003, '산책', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- User 데이터 추가
INSERT INTO users (id, email, name, password, phone, isban, created_date, modified_date, is_deleted)
VALUES
    (1001, 'test@test.com', '홍길동', '11111111', '010-0000-0000', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
    (1002, 'admin@admin.com', '관리자', '11111111', '010-0000-0000', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);


-- PetSitter 데이터 추가
INSERT INTO petsitter (id, name, user_id, address_id, mobile, isaccept, created_date, modified_date, is_deleted)
VALUES (1001, '홍길동', 1001, 1001, '010-0000-0000', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PetSitterPetService 데이터 추가
INSERT INTO petsitter_petservice (id, petsitter_id, petservice_id, hprice, created_date, modified_date, is_deleted)
VALUES (1001, 1001, 1001, 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petsitter_petservice (id, petsitter_id, petservice_id, hprice, created_date, modified_date, is_deleted)
VALUES (1002, 1001, 1002, 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PetSitterPetGroupType 데이터 추가
INSERT INTO petsitter_petgrouptype (id, petsitter_id, petgrouptype_id, created_date, modified_date, is_deleted)
VALUES (1001, 1001, 1001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petsitter_petgrouptype (id, petsitter_id, petgrouptype_id, created_date, modified_date, is_deleted)
VALUES (1002, 1001, 1003, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PetSitterPetSize 데이터 추가
INSERT INTO petsitter_petsize (id, petsitter_id, petsize_id, created_date, modified_date, is_deleted)
VALUES (1001, 1001, 1001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petsitter_petsize (id, petsitter_id, petsize_id, created_date, modified_date, is_deleted)
VALUES (1002, 1001, 1002, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petsitter_petsize (id, petsitter_id, petsize_id, created_date, modified_date, is_deleted)
VALUES (1003, 1001, 1003, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PetCareTime 데이터 추가
INSERT INTO petcaretime (id, petsitter_id, weekday, cate_time, created_date, modified_date, is_deleted)
VALUES (1001, 1001, 'MON', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petcaretime (id, petsitter_id, weekday, cate_time, created_date, modified_date, is_deleted)
VALUES (1002, 1001, 'MON', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petcaretime (id, petsitter_id, weekday, cate_time, created_date, modified_date, is_deleted)
VALUES (1003, 1001, 'MON', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petcaretime (id, petsitter_id, weekday, cate_time, created_date, modified_date, is_deleted)
VALUES (1004, 1001, 'MON', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petcaretime (id, petsitter_id, weekday, cate_time, created_date, modified_date, is_deleted)
VALUES (1005, 1001, 'WED', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petcaretime (id, petsitter_id, weekday, cate_time, created_date, modified_date, is_deleted)
VALUES (1006, 1001, 'WED', 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petcaretime (id, petsitter_id, weekday, cate_time, created_date, modified_date, is_deleted)
VALUES (1007, 1001, 'WED', 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petcaretime (id, petsitter_id, weekday, cate_time, created_date, modified_date, is_deleted)
VALUES (1008, 1001, 'WED', 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- Pet 데이터 추가
INSERT INTO pet (id, name, user_id, petgrouptype_id, petsize, age, created_date, modified_date, is_deleted, image_url)
VALUES (1001, '솜털이', 1001, 1001, 1001, '3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, '/api/uploads/dog1.jpeg');

-- PetSitterReserve 데이터 추가
INSERT INTO petsitter_reserve (id, user_id, petsitter_id, pet_id, petservice_id, begin_time, end_time, price, isaccept, ispaied, created_date, modified_date, is_deleted)
VALUES (1001, 1001, 1001, 1001, 1001, '2025-02-19 06:00:00', '2025-02-19 08:00:00', 100, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PayType 데이터 추가
INSERT INTO paytype (id, name, type, created_date, modified_date, is_deleted)
VALUES (1001, '간편결제', 'EASY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO paytype (id, name, type, created_date, modified_date, is_deleted)
VALUES (1002, '일반결제', 'card', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

INSERT INTO rolegroup (id, groupname, description, created_date, modified_date, is_deleted)
VALUES
    (1001, 'admin', 'Administrator role group', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (1002, 'user', 'Regular user role group', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (1003, 'petsitter', 'Pet sitter role group', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);

INSERT INTO user_rolegroup (id, user_id, rolegroup_id, created_date, modified_date, is_deleted)
VALUES
    (1001, 1001, 1002, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (1002, 1002, 1001, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);
