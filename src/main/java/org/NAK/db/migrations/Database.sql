CREATE TYPE projectState AS ENUM ('PENDING', 'COMPLETED', 'CANCELLED');
CREATE TYPE componentType AS ENUM ('MATERIAL', 'WORKFORCE');

CREATE TABLE client (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL,
    address VARCHAR(100) NOT NULL,
    telephone VARCHAR(50),
    isProfessional BOOLEAN
);

CREATE TABLE project (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL,
    profitMargin DOUBLE PRECISION,
    totalCost DOUBLE PRECISION,
    projectState projectstate,
    clientId UUID,
    FOREIGN KEY (clientId) REFERENCES client(id)
);

CREATE TABLE estimate (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    estimatedAmount DOUBLE PRECISION,
    estimatedDate DATE,
    validityDate DATE,
    accepted BOOLEAN,
    projectId UUID,
    FOREIGN KEY (projectId) REFERENCES project(id)
);

CREATE TABLE component (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    componentType componentType,
    name VARCHAR(50) NOT NULL,
    tvaRate DOUBLE PRECISION,
    unitaryCost DOUBLE PRECISION,
    quantity DOUBLE PRECISION,
    outputFactor DOUBLE PRECISION,
    projectId UUID,
    FOREIGN KEY (projectId) REFERENCES project(id)
);

CREATE TABLE material (
    transportCost DOUBLE PRECISION
) INHERITS (component);

CREATE TABLE workforce (
) INHERITS (component);