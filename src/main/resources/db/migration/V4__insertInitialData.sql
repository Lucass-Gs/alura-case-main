-- Inserir categorias
INSERT INTO Category (name, code, color, `order`) VALUES
('Programação', 'programacao', '#00C86F', 1),
('Front-end', 'frontend', '#6BD1FF', 2),
('Data Science', 'datascience', '#9CD33B', 3),
('Inteligência Artificial', 'categoryia', '#7B71FF', 4),
('DevOps', 'devops', '#F16165', 5),
('UX & Design', 'uxdesign', '#DC6EBE', 6),
('Mobile', 'mobile', '#FFBA05', 7),
('Inovação & Gestão', 'inovacao', '#FF8C2A', 8);

-- Inserir cursos
INSERT INTO courses (name, code, instructor, category, description, status) VALUES
-- Cursos de Programação
('Lógica de Programação', 'logica', 'Paulo Silveira', 'Programação', 'Aprenda os conceitos de lógica de programação', 'ACTIVE'),
('.NET', 'dotnet', 'Paulo Silveira', 'Programação', 'Domine .NET', 'ACTIVE'),
('Automação e Produtividade', 'automate', 'Paulo Silveira', 'Programação', 'Trabalhe com automação', 'ACTIVE'),

-- Cursos de Front-end
('HTML', 'front-html', 'Pedro Marins', 'Front-end', 'Aprenda html', 'ACTIVE'),
('CSS', 'front-css', 'Pedro Marins', 'Front-end', 'Aprenda css', 'ACTIVE'),
('Svelte', 'svelte', 'Pedro Marins', 'Front-end', 'Aprenda svelte', 'ACTIVE'),
('VueJS', 'vuejs', 'Pedro Marins', 'Front-end', 'Aprenda vuejs', 'ACTIVE'),

-- Cursos de Data Science
('SQL e Banco de Dados', 'datas-bd', 'Fernanda Santos', 'Data Science', 'Introdução ao SQL e Banco de Dados', 'ACTIVE'),
('Engenharia de Dados', 'datas-ed', 'Fernanda Santos', 'Data Science', 'Trabalhe com Engenharia de Dados', 'ACTIVE'),
('Análise de dados', 'datas-ad', 'Fernanda Santos', 'Data Science', 'Primeiros passos em Análise de dados', 'ACTIVE'),

-- Cursos de DevOps
('Linux', 'linux', 'Carlos Souza', 'DevOps', 'Aprenda Linux', 'ACTIVE'),
('FinOps', 'finops', 'Carlos Souza', 'DevOps', 'Conceitos avançados de FinOps', 'ACTIVE'),
('Automação de processos', 'process-a', 'Carlos Souza', 'DevOps', 'Primeiros passos com Automação de processos', 'ACTIVE'),

-- Cursos de UX & Design
('UI Design', 'ui-design', 'Ana Costa', 'UX & Design', 'UI Design', 'ACTIVE'),
('Design System', 'uiux-ds', 'Ana Costa', 'UX & Design', 'Design System', 'ACTIVE'),
('UX Writing', 'ux-wr', 'Ana Costa', 'UX & Design', 'UX Writing', 'ACTIVE'),

-- Cursos de Mobile
('Flutter', 'r-native', 'Bruno Lima', 'Mobile', 'Desenvolvimento mobile com Flutter', 'ACTIVE'),
('Android', 'flutter', 'Bruno Lima', 'Mobile', 'Apps mobile Android', 'ACTIVE'),
('iOS', 'ios-native', 'Bruno Lima', 'Mobile', 'Desenvolvimento iOS nativo', 'ACTIVE'),

-- Cursos de Inovação & Gestão
('Agilidade', 'agile', 'Marina Silva', 'Inovação & Gestão', 'Fundamentos das metodologias ágeis', 'ACTIVE'),
('Liderança', 'lead', 'Marina Silva', 'Inovação & Gestão', 'Liderança', 'ACTIVE'),
('Ensino e Aprendizagem', 'teach', 'Marina Silva', 'Inovação & Gestão', 'Estratégias de aprendizagem', 'ACTIVE'),

-- Cursos de Inteligência Artificial
('IA para Criativos', 'ia-cr', 'Roberto Alves', 'Inteligência Artificial', 'IA para criativos e designers', 'ACTIVE'),
('IA para Programação', 'ia-pr', 'Roberto Alves', 'Inteligência Artificial', 'IA aplicada à programação', 'ACTIVE'),
('IA para Negócios', 'ia-bus', 'Roberto Alves', 'Inteligência Artificial', 'IA para estratégias de negócio', 'ACTIVE');
