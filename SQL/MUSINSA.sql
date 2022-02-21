회원테이블, 주문테이블, 보유쿠폰 테이블을 조인해
주문번호, 주문금액, 적립금, 보유쿠폰 SELECT
PAY테이블의 PAY_PRICE

INSERT INTO NOTICE(NOTICE_NO, NOTICE_TITLE, NOTICE_CONTENT, NOTICE_DATE, ADM_ID) 
VALUES(1000001, '배송 공지사항', '택배사가 바빠서 배송이 늦습니다.', SYSDATE, (SELECT ADM_ID FROM ADMIN));


commit;

DELETE FROM NOTICE
 WHERE ;

SELECT C.ORD_NO,
       C.ORD_PRICE,
       A.MEM_MILE,
       B.COP_NO,
       A.MEM_ID
  FROM MEMBER A, HAVE_COUPON B, ORDERS C
 WHERE A.MEM_ID = B.MEM_ID
   AND A.MEM_ID = C.MEM_ID;
   commit;
 SELECT NOTICE_NO,NOTICE_TITLE,NOTICE_DATE,ADM_ID 
			    FROM NOTICE 
				   WHERE ADM_ID = 'system';  
INSERT INTO MEMBER(MEM_ID,MEM_NAME,MEM_PASS,MEM_ADDR,MEM_BIR,MEM_GRADE,MEM_MILE,MEM_BANK,MEM_ACC)
 VALUES('aaa666','대덕이','aaa666','대전광역시 관평동','2000/07/08','MEMBER',1000,'농협',999555111);
 
 INSERT INTO QNA(PR_QNA_NO,PR_QNA_TITLE,PR_QNA_CONTENT,PR_QNA_DATE,MEM_ID,PROD_NO)
   VALUES(2022021601,'궁금합니다.','점심메뉴가 궁금합니다',SYSDATE,'abc123','1000000001');
  INSERT INTO QNA(PR_QNA_NO,PR_QNA_TITLE,PR_QNA_CONTENT,PR_QNA_DATE,MEM_ID,PROD_NO)
   VALUES(2022021602,'두번째질문.','점심메뉴가 궁금합니다',SYSDATE,'aaa666','1000000001');
  INSERT INTO QNA(PR_QNA_NO,PR_QNA_TITLE,PR_QNA_CONTENT,PR_QNA_DATE,MEM_ID,PROD_NO)
   VALUES(2022021603,'세번째질문.','점심메뉴가 궁금합니다',SYSDATE,'aaa666','1000000002');                
   
UPDATE PROD_BOARD B
    SET PROD_RANK = (SELECT A.PRANK
                        FROM (SELECT PROD_NO,
                                     SUM(DEORDER_QTY),
                                     RANK() OVER(ORDER BY SUM(DEORDER_QTY) DESC) AS PRANK
                                FROM DEORDER
                               GROUP BY PROD_NO) A, DEORDER C
                        WHERE A.PROD_NO=C.PROD_NO
                          AND A.PROD_NO=B.PROD_NO)
  WHERE B.PROD_NO  IN(SELECT PROD_NO
                        FROM DEORDER);
                        
                        commit;
                        
                        commit;
                        

   
    