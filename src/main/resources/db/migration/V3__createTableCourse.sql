CREATE TABLE courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(10) NOT NULL UNIQUE,
    instructor VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    inactivation_date DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_course_code_format CHECK (code REGEXP '^[a-zA-Z]+(-[a-zA-Z]+)*$'),
    CONSTRAINT chk_course_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE INDEX idx_course_code ON courses(code);
CREATE INDEX idx_course_status ON courses(status);
CREATE INDEX idx_course_category ON courses(category);
CREATE INDEX idx_course_instructor ON courses(instructor);
