
 INSERT INTO HAVE_COUPON(COP_NO, MEM_ID, COUPON_NO)
 VALUES ('AA0001', 'abc123', 40001);
 
 INSERT INTO DEORDER(PROD_NO, ORD_NO, DEORDER_QTY)
 VALUES (1000000001, (SELECT ORD_NO FROM ORDERS WHERE MEM_ID = 'abc123'), 1);
 
  INSERT INTO DEORDER(PROD_NO, ORD_NO, DEORDER_QTY)
 VALUES (1000000002, (SELECT ORD_NO FROM ORDERS WHERE MEM_ID = 'abc123'), 1);
 
   INSERT INTO DEORDER(PROD_NO, ORD_NO, DEORDER_QTY)
 VALUES (1000000005, (SELECT ORD_NO FROM ORDERS WHERE MEM_ID = 'abc123' AND ORD_NO = 2202170001), 2);
 
  INSERT INTO DEORDER(PROD_NO, ORD_NO, DEORDER_QTY)
 VALUES (1000000020, (SELECT ORD_NO FROM ORDERS WHERE MEM_ID = 'abc123'), 3);
  
   SELECT A.ORD_NO,
		      B.ORD_DATE, 
		      D.MEM_ID, 
	     C.PROD_NAME,
			   C.PROD_COLOR, 
       C.PROD_SIZE, 
		      A.DEORDER_QTY, 
			   C.PROD_COST, 
	        C.PROD_NO 
	   FROM DEORDER A, ORDERS B, PROD_BOARD C, MEMBER D 
			   WHERE A.ORD_NO = B.ORD_NO 
			   AND A.PROD_NO = C.PROD_NO 
			    AND B.MEM_ID = D.MEM_ID 
			     AND B.MEM_ID = 'abc123'
			   AND B.ORD_NO = 2202150001;
               
  UPDATE ORDERS 
     SET ORD_PRICE =
        (SELECT D.PSUM
           FROM (SELECT A.ORD_NO AS AOID,
                        SUM(C.PROD_COST*B.DEORDER_QTY) AS PSUM
                   FROM ORDERS A, DEORDER B, PROD_BOARD C
                  WHERE A.ORD_NO=B.ORD_NO
                    AND B.PROD_NO=C.PROD_NO
                    AND A.ORD_NO=2202150001
                  GROUP BY A.ORD_NO) D)
   WHERE ORD_NO = 2202150001;    
   
   
   
   INSERT INTO PROD_BOARD(PROD_NO, PROD_COLOR, PROD_NAME, PROD_DETAIL, PROD_COST, PROD_SIZE, LPROD_NO)
   VALUES((SELECT NVL(MAX(PROD_NO), 0) + 1 FROM PROD_BOARD WHERE SUBSTR(PROD_NO,1,1) = 1), ?, ?, ?, ? ,?, ?);
   
   
   INSERT INTO PROD_BOARD(PROD_NO, PROD_COLOR, PROD_NAME, PROD_DETAIL, PROD_COST, PROD_SIZE, LPROD_NO)
   VALUES((SELECT NVL(MAX(PROD_NO), 0) + 1 FROM PROD_BOARD WHERE SUBSTR(PROD_NO,1,1) = 1), 'PINK', '가디건', '예쁜 가디건', 80000, 'S', 'A0001'); 
   
   delete from prod_board where prod_no = 1000000121;
   
   commit;
   
   --주문번호, 결제일자, 아이디, 상품이름, 상품단가, 수량, 주문금액, 결제금액
   SELECT B.ORD_NO,  
          B.PAY_DATE, 
          D.MEM_ID, 
          C.PROD_NAME, 
		  C.PROD_COST,
          A.DEORDER_QTY 
     FROM PROD_BOARD A, DEORDER B, ORDERS C, PAY D, MEMBER E 
    WHERE A.PROD_NO = B.PROD_BOARD
	  AND A.PROD_NO = C.PROD_NO 
      AND B.MEM_ID = D.MEM_ID 
	  AND B.MEM_ID = 'abc123';
      
   
   SELECT B.PROD_NO,
          A.PROD_NAME,
          A.PROD_COST,
          B.DEORDER_QTY,
          (A.PROD_COST * B.DEORDER_QTY),
          C.ORD_PRICE,
          C.MEM_ID,
          B.ORD_NO,
          D.PAY_DATE,
          D.PAY_PRICE
     FROM PROD_BOARD A, DEORDER B, ORDERS C, PAY D
    WHERE A.PROD_NO=B.PROD_NO
      AND B.ORD_NO=C.ORD_NO
      AND C.ORD_NO = D.ORD_NO;
      
      
      SELECT B.PROD_NO, "
		       + "             A.PROD_NAME, "
		       + "             A.PROD_COST, "
		       + "             B.DEORDER_QTY, "
		       + "            (A.PROD_COST * B.DEORDER_QTY), "
		       + "             C.ORD_PRICE, "
		       + "             C.MEM_ID, "
		       + "             B.ORD_NO, "
		       + "        FROM PROD_BOARD A, DEORDER B, ORDERS C "
		       + "       WHERE A.PROD_NO=B.PROD_NO "
		       + "         AND B.ORD_NO=C.ORD_NO 
   
   
   SELECT A.MEM_ID
          B.
          C.PAY_PRICE
          C.PAY_DATE
          C.ORD_NO
     FROM MEMBER A, ORDERS B, PAY C
     
  INSERT INTO PAY(ORD_NO,PAY_PRICE,PAY_DATE,MEM_MILE,MEM_COUPON,PAY_TCODE)
 VALUES(2202170001,100000,SYSDATE,1000,'MEMBER','A001');   
     
  INSERT INTO PAY(ORD_NO, PAY_PRICE, PAY_DATE, MEM_MILE, MEM_COUPON, PAY_TCODE)
  VALUES (2202150001, 300000, SYSDATE, 1000, 'MEMBER', 'A001');
   
   SELECT ORD_NO
     FROM ORDERS
   INTERSECT
   SELECT ORD_NO
     FROM PAY;
   
   
   
   
   
   
   
   
   
   
             
  update orders 
     set ord_price = null;
     
     DELETE FROM ORDERS WHERE ORD_NO = '2202150003';
     
   create or replace procedure proc_create_ordno(
   p_mid member.mem_id%type)
 is
   v_ord_no orders.ord_no%type:=0;
   v_date char(7):=to_char(sysdate,'yymmdd')||'%';
   v_flag number:=0;
   v_temp_no number:=0;
 begin
   select count(*) into v_flag
     from orders
    where ord_no like v_date; 
   if v_flag=0 then
      v_ord_no:=to_number(to_char(sysdate,'yymmdd')
                ||trim('0001'));
   else
      select to_number(max(ORD_NO))+1 into v_ord_no
        from orders
       where ord_no like v_date;
   end if;   

   insert into orders(ord_no,mem_id)
     select v_ord_no,p_mid
       from dual;  
  commit;
 end;
 /
 execute proc_create_ordno('abc123');   
     
     
     
     
     
  --PAY에만 있는 주문번호를 출력
 
-- SELECT A.ORD_NO AS PORD_NO
--  FROM ORDERS A LEFT JOIN PAY B
--  ON A.ORD_NO = B.ORD_NO
--  WHERE A.ORD_NO IS NULL
--    AND A.MEM_ID = 'abc123';
    
   SELECT ORD_NO
     FROM ORDERS
   INTERSECT
   SELECT ORD_NO
     FROM PAY;   
 
 --ORDERS, DEORDER에만 있는 주문번호를 가진 주문번호, 상품번호를 출력
 
-- SELECT D.ORD_NO,
--        D.PROD_NO,
--        D.DEORDER_QTY AS PQTY
--   FROM (SELECT A.ORD_NO AS PORD_NO
--           FROM ORDERS A LEFT JOIN PAY B
--             ON A.ORD_NO = B.ORD_NO
--          WHERE B.ORD_NO IS NULL
--            AND A.MEM_ID = 'rkdehdwn') C 
--  INNER JOIN DEORDER D ON C.PORD_NO = D.ORD_NO;
  
  SELECT D.ORD_NO,
         D.PROD_NO,
         D.DEORDER_QTY AS PQTY
   FROM ( SELECT ORD_NO AS PORD_NO
            FROM ORDERS
          INTERSECT
          SELECT ORD_NO
            FROM PAY ) C 
  INNER JOIN DEORDER D ON C.PORD_NO = D.ORD_NO;
                                             --  PAY, ORDERS, DEORDER, PROD_BOARD
 -- ORDERS, PAY에만 있는 주문번호(ORD_NO)를 가진 (결제일(PAY_DATE), 상품명(PROD_NO), 상품금액(PROD_COST), 수량(DEORDER_QTY), 총금액(ORD_PRICE), 총결제금액(PAY_PRICE))를 출력 
 SELECT E.ORD_NO, 
        F.PROD_NAME, 
        F.PROD_COST,
        F.PROD_COLOR, 
        F.PROD_SIZE,
        E.PQTY,
        G.PAY_DATE
   FROM (SELECT D.ORD_NO,
                D.PROD_NO,
                D.DEORDER_QTY AS PQTY
           FROM (SELECT ORD_NO AS PORD_NO
                   FROM ORDERS
              INTERSECT
                 SELECT ORD_NO
                   FROM PAY ) C 
                  INNER JOIN DEORDER D ON C.PORD_NO = D.ORD_NO) E
  INNER JOIN PROD_BOARD F ON E.PROD_NO = F.PROD_NO  
  INNER JOIN PAY G ON C.ORD_NO = G.ORD_NO;
  
  commit;
  
 SELECT E.CONO AS 주문번호,
        E.CADA AS 결제일자,
        E.DPNO AS 상품번호,
        F.PROD_NAME AS 상품명,
        F.PROD_COLOR AS 색상,
        F.PROD_SIZE AS 사이즈,
        E.DDQTY AS 주문수량,
        F.PROD_COST AS 상품단가,
        E.CBPR AS 주문금액,
        E.CAPR AS 결제금액,
        E.CBMID AS 회원번호
   FROM (SELECT D.PROD_NO AS DPNO,
                D.DEORDER_QTY AS DDQTY,
                C.AONO AS CONO,
                C.APR AS CAPR,
                C.BPR AS CBPR,
                C.ADA AS CADA,
                C.BMID AS CBMID
           FROM (SELECT A.ORD_NO AS AONO,
                        A.PAY_PRICE AS APR,
                        B.ORD_PRICE AS BPR,
                        A.PAY_DATE AS ADA, 
                        B.MEM_ID AS BMID
                   FROM PAY A, ORDERS B 
                  WHERE A.ORD_NO=B.ORD_NO
                    AND B.MEM_ID = 'abc123') C, 
                 DEORDER D
          WHERE D.ORD_NO=C.AONO) E, PROD_BOARD F   
  WHERE F.PROD_NO=E.DPNO;    
  
   SELECT E.CONO AS 주문번호,
          E.CADA AS 결제일자,
          E.DPNO AS 상품번호,
          F.PROD_NAME AS 상품명,
          F.PROD_COLOR AS 색상,
          F.PROD_SIZE AS 사이즈,
          E.DDQTY AS 주문수량,
          F.PROD_COST AS 상품단가,
          E.CBPR AS 주문금액,
          E.CAPR AS 결제금액,
          E.CBMID AS 회원번호
    FROM (SELECT D.PROD_NO AS DPNO,
                D.DEORDER_QTY AS DDQTY,
                C.AONO AS CONO,
                C.APR AS CAPR,
                C.BPR AS CBPR,
                C.ADA AS CADA,
                C.BMID AS CBMID
           FROM (SELECT A.ORD_NO AS AONO,
                        A.PAY_PRICE AS APR,
                        B.ORD_PRICE AS BPR,
                        A.PAY_DATE AS ADA, 
                        B.MEM_ID AS BMID
                   FROM PAY A, ORDERS B 
                  WHERE A.ORD_NO=B.ORD_NO
                    AND B.MEM_ID = 'abc123') C, 
                 DEORDER D
          WHERE D.ORD_NO=C.AONO) E, PROD_BOARD F   
  WHERE F.PROD_NO=E.DPNO; 
  
  주문번호에 따른 주문날짜, 상품이름, 상품색상, 사이즈, 수량, 금액
  SELECT A.PAY_DATE, A.ORD_NO, B.PROD_NAME, B.PROD_COLOR, B.PROD_SIZE, C.DEORDER_QTY, B.PROD_COST, E.MEM_MILE, E.MEM_GRADE
    FROM PAY A, PROD_BOARD B, DEORDER C, ORDERS D, MEMBER E
   WHERE A.ORD_NO = D.ORD_NO
     AND D.ORD_NO = C.ORD_NO
     AND C.PROD_NO = B.PROD_NO
     AND D.MEM_ID = E.MEM_ID
     AND D.MEM_ID = 'abc123'
     AND A.ORD_NO = 2202170001;
  
 SELECT A.ORD_NO , 
        A.PAY_PRICE , 
	    B.ORD_PRICE , 
        A.PAY_DATE 
   FROM PAY A, ORDERS B  
  WHERE A.ORD_NO=B.ORD_NO 
	AND B.MEM_ID = 'abc123';    
 
 SELECT A.ORD_NO AS 주문번호, 
        A.PAY_PRICE AS 결제액, 
	    B.ORD_PRICE AS 주문액, 
        A.PAY_DATE AS 결제일 
   FROM PAY A, ORDERS B  
  WHERE A.ORD_NO=B.ORD_NO 
	AND B.MEM_ID = 'abc123';

 commit;
 
 -- 결제금액(PAY_PRICE), 입력받아야 할것(MEM_MILE, MEM_COUPON) --> 자바에서 // HAVE_COUPON 에서 보유한 쿠폰중에서 써야함 DELETE 해줌 // 적립금을 쓰면 잔액을 MEMBER 테이블에 UPDATE
UPDATE PAY
   SET MEM_MILE = ?
 
 
 
 
 
 