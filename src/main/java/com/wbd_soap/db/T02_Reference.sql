CREATE TABLE IF NOT EXISTS Reference (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    anime_account_id INT NOT NULL,
    forum_account_id INT,
    referal_code VARCHAR(20) NOT NULL,
    point INT
);