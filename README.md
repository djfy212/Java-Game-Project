SQL 테이블

CREATE TABLE rankings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    player_name VARCHAR(255),    
    score INT,        -- 점수 (최다 스코어용)    
    time INT,         -- 시간 (최단시간용)    
    ranking_type INT, -- 0: 최다 스코어, 1: 최단 시간    
    UNIQUE(player_name, ranking_type)    
);


Ranking, RankingPanel 추가
Main, GameUI, GamePanel, TitleUI, TitlePanel 수정함
