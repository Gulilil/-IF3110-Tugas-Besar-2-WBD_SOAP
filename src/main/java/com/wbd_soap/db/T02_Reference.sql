CREATE TABLE IF NOT EXISTS reference (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    anime_account_id INT NOT NULL,
    forum_account_id INT,
    referral_code VARCHAR(20) NOT NULL UNIQUE,
    point INT,
    UNIQUE (anime_account_id, forum_account_id, referral_code)
);