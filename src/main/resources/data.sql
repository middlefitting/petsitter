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
VALUES (101, '서울', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO address (id, city, created_date, modified_date, is_deleted)
VALUES (102, '경기', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO address (id, city, created_date, modified_date, is_deleted)
VALUES (103, '부산', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO address (id, city, created_date, modified_date, is_deleted)
VALUES (104, '대전', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PetGroup 데이터 추가
INSERT INTO petgroup (id, groupname, code, created_date, modified_date, is_deleted)
VALUES (101, '강아지', 'DOG', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petgroup (id, groupname, code, created_date, modified_date, is_deleted)
VALUES (102, '고양이', 'CAT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PetGroupType 데이터 추가
INSERT INTO petgrouptype (id, typename, code, petgroup_id, created_date, modified_date, is_deleted)
VALUES (101, '말티즈', 'MALTESE', 101, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petgrouptype (id, typename, code, petgroup_id, created_date, modified_date, is_deleted)
VALUES (102, '푸들', 'POODLE', 101, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petgrouptype (id, typename, code, petgroup_id, created_date, modified_date, is_deleted)
VALUES (103, '페르시안', 'PERSIAN', 102, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petgrouptype (id, typename, code, petgroup_id, created_date, modified_date, is_deleted)
VALUES (104, '러시안블루', 'RUSSIAN_BLUE', 102, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PetSize 데이터 추가
INSERT INTO petsize (id, size_info, created_date, modified_date, is_deleted)
VALUES (101, '소형(10kg 이하)', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petsize (id, size_info, created_date, modified_date, is_deleted)
VALUES (102, '중형(10kg~25kg 이하)', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petsize (id, size_info, created_date, modified_date, is_deleted)
VALUES (103, '대형(25kg 초과)', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PetService 데이터 추가
INSERT INTO petservice (petservice_id, servicename, created_date, modified_date, is_deleted)
VALUES (101, '돌봄', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petservice (petservice_id, servicename, created_date, modified_date, is_deleted)
VALUES (102, '훈련', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petservice (petservice_id, servicename, created_date, modified_date, is_deleted)
VALUES (103, '산책', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- User 데이터 추가
INSERT INTO users (id, email, name, password, phone, isban, created_date, modified_date, is_deleted)
VALUES
    (101, 'test@test.com', '홍길동', '11111111', '010-0000-0000', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
    (102, 'admin@admin.com', '관리자', '11111111', '010-0000-0000', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);


-- PetSitter 데이터 추가
INSERT INTO petsitter (id, name, user_id, address_id, mobile, isaccept, created_date, modified_date, is_deleted)
VALUES (101, '홍길동', 101, 101, '010-0000-0000', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PetSitterPetService 데이터 추가
INSERT INTO petsitter_petservice (id, petsitter_id, petservice_id, hprice, created_date, modified_date, is_deleted)
VALUES (101, 101, 101, 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petsitter_petservice (id, petsitter_id, petservice_id, hprice, created_date, modified_date, is_deleted)
VALUES (102, 101, 102, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PetSitterPetGroupType 데이터 추가
INSERT INTO petsitter_petgrouptype (id, petsitter_id, petgrouptype_id, created_date, modified_date, is_deleted)
VALUES (101, 101, 101, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petsitter_petgrouptype (id, petsitter_id, petgrouptype_id, created_date, modified_date, is_deleted)
VALUES (102, 101, 103, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PetSitterPetSize 데이터 추가
INSERT INTO petsitter_petsize (id, petsitter_id, petsize_id, created_date, modified_date, is_deleted)
VALUES (101, 101, 101, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petsitter_petsize (id, petsitter_id, petsize_id, created_date, modified_date, is_deleted)
VALUES (102, 101, 102, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petsitter_petsize (id, petsitter_id, petsize_id, created_date, modified_date, is_deleted)
VALUES (103, 101, 103, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PetCareTime 데이터 추가
INSERT INTO petcaretime (id, petsitter_id, weekday, cate_time, created_date, modified_date, is_deleted)
VALUES (101, 101, 'MON', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petcaretime (id, petsitter_id, weekday, cate_time, created_date, modified_date, is_deleted)
VALUES (102, 101, 'MON', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petcaretime (id, petsitter_id, weekday, cate_time, created_date, modified_date, is_deleted)
VALUES (103, 101, 'MON', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petcaretime (id, petsitter_id, weekday, cate_time, created_date, modified_date, is_deleted)
VALUES (104, 101, 'MON', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petcaretime (id, petsitter_id, weekday, cate_time, created_date, modified_date, is_deleted)
VALUES (105, 101, 'WED', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petcaretime (id, petsitter_id, weekday, cate_time, created_date, modified_date, is_deleted)
VALUES (106, 101, 'WED', 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petcaretime (id, petsitter_id, weekday, cate_time, created_date, modified_date, is_deleted)
VALUES (107, 101, 'WED', 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO petcaretime (id, petsitter_id, weekday, cate_time, created_date, modified_date, is_deleted)
VALUES (108, 101, 'WED', 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- Pet 데이터 추가
INSERT INTO pet (id, name, user_id, petgrouptype_id, petsize, age, created_date, modified_date, is_deleted, image_url)
VALUES (101, '솜털이', 101, 101, 101, '3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false, '/api/uploads/dog1.jpeg');

-- PetSitterReserve 데이터 추가
INSERT INTO petsitter_reserve (id, user_id, petsitter_id, pet_id, petservice_id, begin_time, end_time, price, isaccept, ispaied, created_date, modified_date, is_deleted)
VALUES (101, 101, 101, 101, 101, '2025-02-19 06:00:00', '2025-02-19 08:00:00', 100, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- PayType 데이터 추가
INSERT INTO paytype (id, name, type, created_date, modified_date, is_deleted)
VALUES (101, '간편결제', 'EASY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);
INSERT INTO paytype (id, name, type, created_date, modified_date, is_deleted)
VALUES (102, '일반결제', 'card', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

INSERT INTO rolegroup (id, groupname, description, created_date, modified_date, is_deleted)
VALUES
    (101, 'admin', 'Administrator role group', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (102, 'user', 'Regular user role group', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (103, 'petsitter', 'Pet sitter role group', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);

INSERT INTO user_rolegroup (id, user_id, rolegroup_id, created_date, modified_date, is_deleted)
VALUES
    (101, 101, 102, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (102, 102, 101, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);
