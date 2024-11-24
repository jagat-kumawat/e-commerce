-- passwords are in the formal : password<UserLetter>123.Unless specified otherwise.
INSERT INTO local_user (email,first_name,last_name,password,username,email_verified)
VALUES ('user1@junit.com','user1-firstname','user1-lastname','$2a$10$Kj.p5p4bBTDnmKui.xFJTeYiustnMfZNeZXU5ap0X4IL0NZ6/2h9C','user1',true),
('user2@junit.com','user2-firstname','user2-lastname','$2a$10$Kj.p5p4bBTDnmKui.xFJTeYiustnMfZNeZXU5ap0X4IL0NZ6/2h9C','user2',false)
