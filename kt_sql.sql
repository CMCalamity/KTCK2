CREATE DATABASE IF NOT EXISTS erp_system;

USE erp_system;

-- Bảng Users (Quản lý người dùng - Yêu cầu đề thi)
CREATE TABLE IF NOT EXISTS Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- **QUAN TRỌNG: CẦN MÃ HÓA (trong Java)**
    role VARCHAR(50) NOT NULL DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL
);

-- Bảng Departments_Types (Phòng ban - Liên quan đến Employees)
CREATE TABLE IF NOT EXISTS Departments_Types (
    id INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(255) NOT NULL,
    description TEXT
);

-- Bảng Employees (Nhân viên - Core của HR Module)
CREATE TABLE IF NOT EXISTS Employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    hire_date DATE,
    department_id INT,
    FOREIGN KEY (department_id) REFERENCES Departments_Types(id) ON DELETE SET NULL -- Xử lý khi phòng ban bị xóa
);

-- Bảng Leave_Requests (Đơn xin nghỉ phép - Time & Attendance)
CREATE TABLE IF NOT EXISTS Leave_Requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    leave_type VARCHAR(50) NOT NULL,
    reason TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'Pending', -- Trạng thái: Pending, Approved, Rejected
    FOREIGN KEY (employee_id) REFERENCES Employees(id) ON DELETE CASCADE -- Xử lý khi nhân viên bị xóa
);

-- Bảng Customers (Khách hàng - Core của Sales Module)
CREATE TABLE IF NOT EXISTS Customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    contact_name VARCHAR(255),
    address VARCHAR(255),
    phone VARCHAR(20),
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Sales_Orders (Đơn hàng - Sales Module)
CREATE TABLE IF NOT EXISTS Sales_Orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    order_date DATE NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Pending', -- Trạng thái: Pending, Processing, Shipped, Completed
    FOREIGN KEY (customer_id) REFERENCES Customers(id) ON DELETE SET NULL
);

-- Bảng Inventory_Items (Sản phẩm - Core của Inventory Module)
CREATE TABLE IF NOT EXISTS Inventory_Items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL,
    description TEXT,
    unit_price DECIMAL(10, 2) NOT NULL,
    quantity_in_stock INT NOT NULL DEFAULT 0,
    reorder_level INT
);

-- Bảng Vendors (Nhà cung cấp - Core của Purchasing Module)
CREATE TABLE IF NOT EXISTS Vendors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vendor_name VARCHAR(255) NOT NULL,
    contact_person VARCHAR(255),
    address VARCHAR(255),
    phone VARCHAR(20),
    email VARCHAR(100)
);

-- Bảng Purchase_Orders (Đơn mua - Purchasing Module)
CREATE TABLE IF NOT EXISTS Purchase_Orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vendor_id INT,
    order_date DATE NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    delivery_date DATE,
    FOREIGN KEY (vendor_id) REFERENCES Vendors(id) ON DELETE SET NULL
);

-- Bảng Projects (Dự án - Core của Project Management Module)
CREATE TABLE IF NOT EXISTS Projects (
    id INT AUTO_INCREMENT PRIMARY KEY,
    project_name VARCHAR(255) NOT NULL,
    start_date DATE,
    end_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'Planned',
    budget DECIMAL(12, 2)
);

-- Bảng Tasks (Công việc - Project Management Module)
CREATE TABLE IF NOT EXISTS Tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    project_id INT,
    task_name VARCHAR(255) NOT NULL,
    assigned_to INT, -- employee_id
    due_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'To Do',
    FOREIGN KEY (project_id) REFERENCES Projects(id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_to) REFERENCES Employees(id) ON DELETE SET NULL
);

-- Dữ liệu mẫu (Tùy chọn - Mở rộng hơn)
INSERT INTO Departments_Types (department_name, description) VALUES
('Tài chính', 'Quản lý tài chính và kế toán'),
('Marketing', 'Xây dựng thương hiệu và quảng bá sản phẩm'),
('Công nghệ thông tin', 'Quản lý hệ thống và hỗ trợ kỹ thuật'),
('Sản xuất', 'Điều hành quy trình sản xuất'),
('Quản lý dự án', 'Lập kế hoạch và thực hiện dự án'),
('Chăm sóc khách hàng', 'Hỗ trợ và giải quyết vấn đề của khách hàng'),
('Nghiên cứu và phát triển', 'Nghiên cứu và phát triển sản phẩm mới'),
('Hành chính', 'Quản lý các hoạt động hành chính'),
('Nhân sự', 'Quản lý nhân viên và tuyển dụng'),
('Kinh doanh', 'Bán hàng và phát triển thị trường');

INSERT INTO Employees (first_name, last_name, email, phone, hire_date, department_id) VALUES
('Nguyễn', 'Văn An', 'an.nguyen@company.com', '0901234567', '2020-05-10', 1),
('Trần', 'Thị Bình', 'binh.tran@company.com', '0909876543', '2021-02-15', 2),
('Lê', 'Minh Cường', 'cuong.le@company.com', '0933444555', '2019-11-20', 3),
('Phạm', 'Hoàng Dũng', 'dung.pham@company.com', '0977112233', '4024-08-01', 4),
('Hoàng', 'Thị Yến', 'yen.hoang@company.com', '0911223344', '2022-09-05', 5),
('Vũ', 'Đức Duy', 'duy.vu@company.com', '0944555666', '2023-01-08', 6),
('Đặng', 'Thị Thúy Hà', 'ha.dang@company.com', '0988666777', '2021-07-12', 7),
('Bùi', 'Quang Huy', 'huy.bui@company.com', '0922777888', '2020-12-24', 8),
('Cao', 'Thị Kim Liên', 'lien.cao@company.com', '0966888999', '2022-04-18', 9),
('Phan', 'Thanh Tùng', 'tung.phan@company.com', '0955999000', '2023-06-30', 10);

INSERT INTO Customers (customer_name, contact_name, address, phone, email) VALUES
('Công ty TNHH ABC', 'Trần Văn An', '99 Nguyễn Thị Minh Khai, Quận 1, TP.HCM', '028-38221122', 'abc@companyx.com'),
('Cửa hàng Điện Máy XYZ', 'Lê Thị Bình', '15 Hai Bà Trưng, Quận 3, TP.HCM', '0903-888-999', 'xyz_store@email.vn'),
('Siêu thị CoopMart', 'Phạm Hoàng Cường', '300 Lê Văn Sỹ, Tân Bình, TP.HCM', '028-39990000', 'coopmart.online@coop.vn'),
('Nhà sách Phương Nam', 'Nguyễn Thị Diệu', '55 Trần Hưng Đạo, Quận 5, TP.HCM', '028-38334455', 'pn_books@phuongnam.com'),
('Công ty Cổ phần Thực phẩm Sài Gòn', 'Trần Anh Tuấn', '10 Cộng Hòa, Tân Phú, TP.HCM', '0912-345-678', 'saigonfood@hcm.vn'),
('Khách sạn Majestic', 'Lê Hoàng Lan', '01 Đồng Khởi, Quận 1, TP.HCM', '028-38295511', 'reservation@majestichotel.com'),
('Trường Đại học Bách Khoa', 'Phạm Minh Đức', '268 Lý Thường Kiệt, Quận 10, TP.HCM', '028-38647256', 'info@hcmut.edu.vn'),
('Bệnh viện Chợ Rẫy', 'Nguyễn Thị Ngọc', '201B Nguyễn Chí Thanh, Quận 5, TP.HCM', '028-38554137', 'chpv@choray.vn'),
('Trung tâm Thương mại Vincom', 'Trần Văn Hùng', '70 Lê Thánh Tôn, Quận 1, TP.HCM', '028-39369999', 'vincom.center@vincom.com.vn'),
('Công ty Du lịch Vietravel', 'Lê Thị Thu Hương', '190 Pasteur, Quận 3, TP.HCM', '028-38220088', 'info@vietravel.com');

INSERT INTO Sales_Orders (customer_id, order_date, total_amount, status) VALUES
(1, '2024-04-15', 2000000, 'Completed'),
(2, '2024-04-22', 3000000, 'Shipped'),
(3, '2024-04-29', 1800000, 'Processing'),
(1, '2024-06-10', 2200000, 'Completed'),
(2, '2024-06-18', 2800000, 'Shipped'),
(3, '2024-06-25', 1500000, 'Processing'),
(1, '2024-07-05', 3500000, 'Completed'),
(2, '2024-07-12', 4000000, 'Shipped'),
(3, '2024-07-19', 2100000, 'Processing'),
(1, '2024-08-02', 2600000, 'Completed'),
(2, '2024-08-09', 3200000, 'Shipped'),
(3, '2024-08-16', 1900000, 'Processing'),
(1, '2024-09-08', 2900000, 'Completed'),
(2, '2024-09-15', 3800000, 'Shipped'),
(3, '2024-09-22', 2300000, 'Processing');

INSERT INTO Inventory_Items (item_name, description, unit_price, quantity_in_stock, reorder_level) VALUES
('Bàn làm việc giám đốc', 'Bàn gỗ tự nhiên cao cấp, thiết kế sang trọng', 15000000, 10, 5),
('Ghế xoay văn phòng', 'Ghế da êm ái, có tựa đầu', 800000, 50, 20),
('Máy tính để bàn Dell', 'Cấu hình mạnh mẽ, màn hình 27 inch', 12000000, 15, 3),
('Máy in HP LaserJet', 'In hai mặt tự động, tốc độ cao', 5000000, 10, 2),
('Giấy A4 Double A', 'Giấy trắng, mịn, định lượng 80gsm', 50000, 200, 50),
('Bút bi Thiên Long', 'Bút viết trơn tru, mực ra đều', 5000, 500, 100),
('File hồ sơ A4', 'File nhựa trong suốt, đựng 100 tờ', 10000, 150, 30),
('Máy chiếu Epson', 'Độ phân giải cao, hình ảnh sắc nét', 10000000, 5, 1),
('Màn hình máy tính Samsung', 'Màn hình cong, 32 inch', 8000000, 20, 5),
('Bảng trắng treo tường', 'Bảng từ, dễ dàng lau chùi', 1000000, 30, 10);

INSERT INTO Vendors (vendor_name, contact_person, address, phone, email) VALUES
('Công ty TNHH Thiết bị Văn phòng A', 'Nguyễn Văn Bình', '45 Lê Duẩn, Quận 1, TP.HCM', '028-38290123', 'sales@thietbivanphonga.vn'),
('Công ty Cổ phần Nội thất B', 'Trần Thị Cẩm Vân', '123 Điện Biên Phủ, Bình Thạnh, TP.HCM', '0909-123-456', 'contact@noithatb.com'),
('Công ty TNHH Giải pháp Công nghệ C', 'Phạm Đức Anh', '27 Nguyễn Tri Phương, Quận 5, TP.HCM', '028-38335678', 'info@techsolutionsc.vn'),
('Công ty TNHH Dịch vụ Vận chuyển D', 'Lê Minh Hoàng', '88 Võ Văn Tần, Quận 3, TP.HCM', '0912-987-654', 'shippingd@dichvuvanchuyen.com'),
('Công ty Cổ phần In ấn E', 'Hoàng Thị Ngọc Lan', '35 Trần Phú, Tân Phú, TP.HCM', '028-38121314', 'printinge@inan.vn'),
('Công ty TNHH Cung cấp Vật tư F', 'Nguyễn Thị Thùy Dung', '100 Trường Chinh, Tân Bình, TP.HCM', '028-39485769', 'suppliesf@vatuf.com'),
('Công ty Cổ phần Thiết kế G', 'Trần Văn Mạnh', '50 Nguyễn Huệ, Quận 1, TP.HCM', '0933-444-555', 'designg@thietke.vn'),
('Công ty TNHH Quảng cáo H', 'Lê Thị Hà', '20 Lê Lai, Quận 1, TP.HCM', '028-38246810', 'advertingh@quangcao.com'),
('Công ty Cổ phần Phần mềm I', 'Phạm Hoàng Nam', '15 Kỳ Đồng, Quận 3, TP.HCM', '0977-111-222', 'softwarei@phanmem.vn'),
('Công ty TNHH Tư vấn J', 'Nguyễn Thị Hồng', '30 Hoàng Diệu, Quận 4, TP.HCM', '028-39405060', 'consultingj@tuvankienthuc.com');

INSERT INTO Purchase_Orders (vendor_id, order_date, total_amount, delivery_date) VALUES
(1, '2024-05-01', 5000000, '2024-05-10'),
(2, '2024-05-10', 12000000, '2024-05-20'),
(3, '2024-05-20', 8000000, '2024-05-30'),
(1, '2024-06-01', 7000000, '2024-06-10'),
(2, '2024-06-10', 15000000, '2024-06-20'),
(3, '2024-06-20', 10000000, '2024-06-30'),
(1, '2024-07-01', 6000000, '2024-07-10'),
(2, '2024-07-10', 13000000, '2024-07-20'),
(3, '2024-07-20', 9000000, '2024-07-30'),
(1, '2024-08-01', 8000000, '2024-08-10');

INSERT INTO Projects (project_name, start_date, end_date, status, budget) VALUES
('Nâng cấp hệ thống ERP', '2024-01-01', '2024-12-31', 'In Progress', 150000000),
('Phát triển ứng dụng di động bán hàng', '2024-03-15', '2024-07-31', 'In Progress', 80000000),
('Xây dựng website thương mại điện tử', '2024-05-01', '2024-09-30', 'Planned', 120000000),
('Triển khai hệ thống CRM', '2024-07-15', '2025-02-28', 'Planned', 90000000),
('Tối ưu hóa quy trình sản xuất', '2024-09-01', '2025-01-31', 'Completed', 60000000),
('Nghiên cứu thị trường và phát triển sản phẩm mới', '2024-11-01', '2025-03-31', 'Completed', 40000000),
('Cải thiện hệ thống quản lý kho', '2025-01-01', '2025-05-31', 'In Progress', 50000000),
('Nâng cấp cơ sở hạ tầng mạng', '2025-03-01', '2025-07-31', 'Planned', 70000000),
('Phát triển hệ thống báo cáo tài chính', '2025-05-01', '2025-09-30', 'Planned', 100000000),
('Tổ chức sự kiện ra mắt sản phẩm', '2025-07-01', '2025-08-31', 'Completed', 20000000);

INSERT INTO Tasks (project_id, task_name, assigned_to, due_date, status) VALUES
(1, 'Phân tích yêu cầu', 1, '2024-01-15', 'Completed'),
(1, 'Thiết kế hệ thống', 2, '2024-02-28', 'Completed'),
(1, 'Lập trình module HR', 3, '2024-04-30', 'In Progress'),
(2, 'Thiết kế giao diện', 4, '2024-04-01', 'Completed'),
(2, 'Lập trình trang chủ', 1, '2024-05-15', 'In Progress'),
(3, 'Thu thập yêu cầu', 5, '2024-05-10', 'Completed'),
(3, 'Phân tích nghiệp vụ', 2, '2024-06-15', 'In Progress');

INSERT INTO Leave_Requests (employee_id, start_date, end_date, leave_type, reason, status) VALUES
(1, '2024-06-01', '2024-06-03', 'Nghỉ ốm', 'Bị cảm cúm', 'Pending'),
(2, '2024-06-05', '2024-06-05', 'Nghỉ phép năm', 'Đi du lịch với gia đình', 'Approved'),
(3, '2024-06-10', '2024-06-12', 'Nghỉ việc riêng', 'Giải quyết việc cá nhân', 'Rejected'),
(4, '2024-06-15', '2024-06-15', 'Nghỉ bù', 'Làm việc cuối tuần', 'Approved'),
(1, '2024-06-20', '2024-06-22', 'Nghỉ không lương', 'Việc đột xuất', 'Pending'),
(2, '2024-06-25', '2024-06-25', 'Nghỉ phép năm', 'Nghỉ bù lễ', 'Approved'),
(3, '2024-06-28', '2024-06-30', 'Nghỉ việc riêng', 'Đi đám cưới', 'Pending'),
(5, '2024-07-01', '2024-07-02', 'Nghỉ ốm', 'Đau đầu', 'Approved'),
(1, '2024-07-05', '2024-07-07', 'Nghỉ phép năm', 'Nghỉ hè', 'Approved'),
(2, '2024-07-10', '2024-07-10', 'Nghỉ bù', 'Làm thêm giờ', 'Approved'),
(4, '2024-07-12', '2024-07-14', 'Nghỉ việc riêng', 'Chăm sóc người thân', 'Pending'),
(5, '2024-07-15', '2024-07-16', 'Nghỉ không lương', 'Đi công việc', 'Rejected'),
(3, '2024-07-20', '2024-07-21', 'Nghỉ ốm', 'Sốt cao', 'Approved'),
(1, '2024-07-25', '2024-07-27', 'Nghỉ phép năm', 'Nghỉ mát', 'Approved'),
(2, '2024-08-01', '2024-08-01', 'Nghỉ bù', 'Trực đêm', 'Approved'),
(4, '2024-08-05', '2024-08-07', 'Nghỉ việc riêng', 'Đi khám bệnh', 'Pending'),
(5, '2024-08-10', '2024-08-12', 'Nghỉ không lương', 'Việc gia đình', 'Rejected'),
(3, '2024-08-15', '2024-08-16', 'Nghỉ ốm', 'Đau bụng', 'Approved'),
(1, '2024-08-20', '2024-08-22', 'Nghỉ phép năm', 'Đi nghỉ dưỡng', 'Approved'),
(2, '2024-08-25', '2024-08-25', 'Nghỉ bù', 'Làm ca đêm', 'Approved'),
(3, '2024-08-28', '2024-08-30', 'Nghỉ việc riêng', 'Đi du lịch', 'Pending'),
(4, '2024-09-01', '2024-09-03', 'Nghỉ ốm', 'Viêm họng', 'Approved'),
(5, '2024-09-05', '2024-09-05', 'Nghỉ phép năm', 'Đi chơi', 'Approved');

INSERT INTO Users (username, password, role) VALUES
('admin', 'admin123', 'admin'),
('nhanvien1', 'nv123', 'user'),
('nhanvien2', 'nv456', 'user'),
('quanly1', 'ql789', 'manager'),
('ketoan1', 'kt123', 'accountant'),
('it1', 'it456', 'it'),
('truongphong_hr', 'hr789', 'manager'),
('kinhdoanh1', 'kd123', 'sales'),
('khachhang1', 'kh123', 'customer'),
('giamdoc', 'gd123', 'director'),
('thukho', 'tk123', 'inventory'),
('muahang1', 'mh123', 'purchaser'),
('marketing1', 'mk123', 'marketing'),
('baove1', 'bv123', 'security'),
('laocong1', 'lc123', 'worker'),
('tester1', 'ts123', 'tester'),
('developer1', 'dv123', 'developer'),
('designer1', 'ds123', 'designer'),
('phanmem1', 'pm123', 'software'),
('donhang1', 'dh123', 'order');
