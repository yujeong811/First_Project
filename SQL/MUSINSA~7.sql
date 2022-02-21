SELECT A.ORD_NO,
       B.ORD_DATE, 
       D.MEM_ID,
       C.PROD_NAME,
       C.PROD_COST    
  FROM DEORDER A, ORDERS B, PROD_BOARD C, MEMBER D
 WHERE A.ORD_NO = B.ORD_NO
   AND A.PROD_NO = C.PROD_NO
   AND B.MEM_ID = D.MEM_ID
   AND B.MEM_ID = 'abc123';
   
INSERT INTO DEORDER(PROD_NO, ORD_NO, DEORDER_QTY)
SELECT 1000000001, ORD_NO 
   
alter table REVIEW add PROD_NO VARCHAR2(10);
alter table REVIEW add foreign key(PROD_NO) references PROD_BOARD(PROD_NO);

alter table QNA add PROD_NO VARCHAR2(10);
alter table QNA add foreign key(PROD_NO) references PROD_BOARD(PROD_NO);

COMMIT;
       
    INSERT INTO REVIEW(REV_NO,REV_TITLE,REV_CONTENT,REV_DATE,REV_STAR) 
    VALUES((SELECT NVL(MAX(REV_NO), 0) + 1 FROM REVIEW),'����','�븸�� �籸���ǻ� ������ ����',SYSDATE,5);   
    
    
SELECT B.REV_NO AS �����ȣ, 
       D.MEM_ID AS ȸ�����̵�, 
       C.PROD_NO AS ��ǰ��ȣ, 
       A.PROD_NAME AS ��ǰ��, 
       B.REV_TITLE AS ��������, 
       B.REV_DATE AS �ۼ�����
  FROM PROD_BOARD A, REVIEW B, DEORDER C, MEMBER D
 WHERE B.MEM_ID = D.MEM_ID
   AND A.PROD_NO = B.PROD_NO
   AND A.PROD_NO = C.PROD_NO
   AND D.MEM_ID = 'abc123';
   
   
UPDATE REVIEW SET REV_TITLE = '�ȳ�' , REV_CONTENT = '����' , REV_STAR = 1
 WHERE REV_NO = 2 AND MEM_ID = 'abc123';
 commit;
 
DELETE FROM REVIEW
 WHERE REV_NO = 2
   AND MEM_ID = 'abc123';
 
 
 