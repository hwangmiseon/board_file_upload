CREATE TABLE attach(
atch_no NUMBER NOT NULL, -- ÷������ ��ȣ(PK)
atch_parent_no NUMBER NOT NULL, -- �θ���� PK
atch_category VARCHAR2(30) NOT NULL, -- ������ ���� (BOARD, FREE, QNA, PDS �� )
atch_file_name VARCHAR2(100), -- ���� ����� ���ϸ�
atch_original_name VARCHAR2(200) NOT NULL, -- ����ڰ� �ø� ���� ���ϸ�
atch_file_size NUMBER DEFAULT 0, -- ���� ������
atch_fancy_size VARCHAR2(10), -- �ҽ� ������
atch_content_type VARCHAR2(100), -- ������ Ÿ��
atch_path VARCHAR2(100) , -- ���� ��� (board/2020) 
atch_down_hit NUMBER(10) DEFAULT 0, -- �ٿ�ε� Ƚ��
atch_del_yn CHAR(1) DEFAULT 'N', -- �������� (���� �����쿡 ���ؼ� ����ó��)
atch_reg_date DATE DEFAULT SYSDATE, -- �����
CONSTRAINT pk_attach PRIMARY KEY (atch_no));

COMMENT ON TABLE attach is '÷������ ���̺�';
COMMENT ON COLUMN attach.atch_no IS '÷������ ��ȣ(PK)';
COMMENT ON COLUMN attach.atch_parent_no IS '�θ���� PK ';
COMMENT ON COLUMN attach.atch_category IS '������ �з�(BOARD, FREE, QNA, PDS ��)';
COMMENT ON COLUMN attach.atch_file_name IS '���� ����� ���ϸ�';
COMMENT ON COLUMN attach.atch_original_name IS '����ڰ� �ø� ���� ���ϸ�';
COMMENT ON COLUMN attach.atch_file_size IS '���� ������';
COMMENT ON COLUMN attach.atch_fancy_size IS '�ҽ� ������';
COMMENT ON COLUMN attach.atch_content_type IS '������ Ÿ��';
COMMENT ON COLUMN attach.atch_path IS '���� ���(board/2020) ';
COMMENT ON COLUMN attach.atch_down_hit IS '�ٿ�ε� Ƚ��';
COMMENT ON COLUMN attach.atch_del_yn IS '��������(�����쿡 ���ؼ� ���ϻ���ó��)';
COMMENT ON COLUMN attach.atch_reg_date IS '�����';
-- ÷������ ��ȣ(PK)�� ���� ������ ����
CREATE SEQUENCE seq_attach ;