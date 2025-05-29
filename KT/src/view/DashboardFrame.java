package view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import DAO.DepartmentDAO;
import DAO.EmployeeDAO;
import DAO.InventoryItemDAO;
import DAO.LeaveRequestDAO;
import DAO.ProjectDAO;
import DAO.PurchaseOrderDAO;
import DAO.SalesOrderDAO;
import DAO.TaskDAO;
import DAO.VendorDAO;
import DAO.BaseDAO.DataAccessException;

import model.User;
import model.Employee;
import model.Department;
import model.SalesOrder;
import model.InventoryItem;
import model.PurchaseOrder;
import model.Vendor;
import model.Project;
import model.LeaveRequest;
import model.Task;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DashboardFrame extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(DashboardFrame.class.getName());
    private User currentUser;
    private JTabbedPane tabbedPane;

    // Font và màu sắc chung
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font TAB_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color CHART_BG_COLOR = new Color(255, 255, 255);
    private static final Color PRIMARY_COLOR = new Color(79, 129, 189);

    // Các component cho bộ lọc thời gian
    private JComboBox<String> salesTimeFilter;
    private JPanel salesChartPanelContainer;

    public DashboardFrame(User user) {
        this.currentUser = user;

        String title = "Bảng Điều Khiển";
        if (currentUser != null && currentUser.getUsername() != null) {
            title += " - Chào mừng " + currentUser.getUsername();
        }
        setTitle(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(BACKGROUND_COLOR);
        JLabel headerLabel = new JLabel(title);
        headerLabel.setFont(TITLE_FONT);
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel summaryPanel = createSummaryPanel();
        add(summaryPanel, BorderLayout.PAGE_START);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(TAB_FONT);
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Tab 1: Nhân viên (Pie Chart)
        tabbedPane.addTab("Nhân Viên", null, createEmployeePieChartPanel(), "Phân bổ nhân viên theo phòng ban");
        // Tab 2: Doanh số (Bar Chart)
        salesChartPanelContainer = createBasePanel();
        JPanel salesControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        salesTimeFilter = new JComboBox<>(new String[]{"Tháng này", "Quý này", "Năm nay", "Tất cả"});
        salesTimeFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSalesBarChartPanel((String) salesTimeFilter.getSelectedItem());
            }
        });
        salesControlPanel.add(new JLabel("Xem theo: "));
        salesControlPanel.add(salesTimeFilter);
        salesChartPanelContainer.add(salesControlPanel, BorderLayout.NORTH);
        salesChartPanelContainer.add(createSalesBarChartPanel("Tháng này"), BorderLayout.CENTER);
        tabbedPane.addTab("Doanh Số", null, salesChartPanelContainer, "Tổng doanh số bán hàng theo thời gian");

        // Tab 3: Tồn kho (Bar Chart)
        tabbedPane.addTab("Tồn Kho", null, createInventoryBarChartPanel(), "Số lượng hàng tồn kho");
        // Tab 4: Đơn mua (Bar Chart)
        tabbedPane.addTab("Đơn Mua", null, createPurchaseOrderBarChartPanel(), "Tổng giá trị đơn hàng mua theo nhà cung cấp");
        // Tab 5: Dự án (Pie Chart)
        tabbedPane.addTab("Dự Án", null, createProjectPieChartPanel(), "Phân bổ dự án theo trạng thái");
        // Tab 6: Nghỉ phép (Pie Chart)
        tabbedPane.addTab("Nghỉ Phép", null, createLeaveRequestPieChartPanel(), "Phân bổ đơn xin nghỉ phép theo trạng thái");
        // Tab 7: Công việc (Pie Chart)
        tabbedPane.addTab("Công Việc", null, createTaskPieChartPanel(), "Phân bổ công việc theo trạng thái");

        add(tabbedPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(BACKGROUND_COLOR);
        JButton refreshButton = new JButton("Làm mới");
        styleButton(refreshButton);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshAllTabs();
            }
        });
        bottomPanel.add(refreshButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setSize(1200, 850); // Tăng chiều cao một chút để có chỗ cho nút làm mới
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void refreshAllTabs() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        tabbedPane.removeAll();

        // Thêm lại các tab với dữ liệu mới
        tabbedPane.addTab("Nhân Viên", null, createEmployeePieChartPanel(), "Phân bổ nhân viên theo phòng ban");

        salesChartPanelContainer = createBasePanel();
        JPanel salesControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        salesTimeFilter = new JComboBox<>(new String[]{"Tháng này", "Quý này", "Năm nay", "Tất cả"});
        salesTimeFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSalesBarChartPanel((String) salesTimeFilter.getSelectedItem());
            }
        });
        salesControlPanel.add(new JLabel("Xem theo: "));
        salesControlPanel.add(salesTimeFilter);
        salesChartPanelContainer.add(salesControlPanel, BorderLayout.NORTH);
        salesChartPanelContainer.add(createSalesBarChartPanel((String) salesTimeFilter.getSelectedItem()), BorderLayout.CENTER);
        tabbedPane.addTab("Doanh Số", null, salesChartPanelContainer, "Tổng doanh số bán hàng theo thời gian");

        tabbedPane.addTab("Tồn Kho", null, createInventoryBarChartPanel(), "Số lượng hàng tồn kho");
        tabbedPane.addTab("Đơn Mua", null, createPurchaseOrderBarChartPanel(), "Tổng giá trị đơn hàng mua theo nhà cung cấp");
        tabbedPane.addTab("Dự Án", null, createProjectPieChartPanel(), "Phân bổ dự án theo trạng thái");
        tabbedPane.addTab("Nghỉ Phép", null, createLeaveRequestPieChartPanel(), "Phân bổ đơn xin nghỉ phép theo trạng thái");
        tabbedPane.addTab("Công Việc", null, createTaskPieChartPanel(), "Phân bổ công việc theo trạng thái");

        tabbedPane.setSelectedIndex(selectedIndex); // Giữ lại tab đang chọn
        JOptionPane.showMessageDialog(this, "Dữ liệu đã được làm mới.", "Làm mới", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Lấy dữ liệu và hiển thị (cần implement các DAO tương ứng)
        int totalEmployees = 0;
        int totalInventoryItems = 0;
        double totalSales = 0.0;

        EmployeeDAO employeeDAO = null;
        InventoryItemDAO inventoryItemDAO = null;
        SalesOrderDAO salesOrderDAO = null;

        try {
            employeeDAO = new EmployeeDAO();
            inventoryItemDAO = new InventoryItemDAO();
            salesOrderDAO = new SalesOrderDAO();

            totalEmployees = employeeDAO.getAll().size();
            totalInventoryItems = inventoryItemDAO.getAll().size();
            totalSales = salesOrderDAO.getAll().stream().mapToDouble(SalesOrder::getTotalAmount).sum();

        } catch (DataAccessException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy dữ liệu tóm tắt: " + e.getMessage(), e);
        } finally {
            closeDAO(employeeDAO);
            closeDAO(inventoryItemDAO);
            closeDAO(salesOrderDAO);
        }

        panel.add(createSummaryItem("Tổng Nhân Viên:", String.valueOf(totalEmployees), "Số lượng nhân viên hiện tại"));
        panel.add(createSummaryItem("Tổng Sản Phẩm:", String.valueOf(totalInventoryItems), "Tổng số lượng sản phẩm trong kho"));
        panel.add(createSummaryItem("Tổng Doanh Số:", String.format("%.2f VNĐ", totalSales), "Tổng doanh số bán hàng đã ghi nhận"));

        return panel;
    }

    private JPanel createSummaryItem(String labelText, String valueText, String tooltipText) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        itemPanel.setBackground(CHART_BG_COLOR);
        itemPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
        itemPanel.setPreferredSize(new Dimension(180, 80));
        itemPanel.setMaximumSize(new Dimension(180, 80));
        itemPanel.setToolTipText(tooltipText); // Thêm tooltip

        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel value = new JLabel(valueText);
        value.setFont(SUBTITLE_FONT);
        value.setForeground(PRIMARY_COLOR);
        value.setAlignmentX(Component.CENTER_ALIGNMENT);

        itemPanel.add(Box.createVerticalGlue());
        itemPanel.add(label);
        itemPanel.add(Box.createVerticalStrut(5));
        itemPanel.add(value);
        itemPanel.add(Box.createVerticalGlue());

        return itemPanel;
    }


    private void styleButton(JButton button) {
        button.setFont(LABEL_FONT);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
    }

    private JPanel createEmployeePieChartPanel() {
        JPanel panel = createBasePanel();

        DefaultPieDataset dataset = new DefaultPieDataset();
        EmployeeDAO employeeDAO = null;
        DepartmentDAO departmentDAO = null;
        int totalEmployees = 0;

        try {
            employeeDAO = new EmployeeDAO();
            departmentDAO = new DepartmentDAO();

            Map<Integer, String> departmentNames = departmentDAO.getAll().stream()
                    .collect(Collectors.toMap(Department::getId, Department::getDepartmentName));

            Map<String, Integer> countMap = new HashMap<>();
            List<Employee> employees = employeeDAO.getAll();
            totalEmployees = employees.size();

            LOGGER.log(Level.INFO, "Số lượng nhân viên lấy từ DAO: " + employees.size());

            for (Employee emp : employees) {
                String departmentName = "Không xác định";
                if (emp.getDepartmentId() > 0 && departmentNames.containsKey(emp.getDepartmentId())) {
                    departmentName = departmentNames.get(emp.getDepartmentId());
                } else {
                    LOGGER.log(Level.WARNING, "Không tìm thấy phòng ban hoặc ID phòng ban không hợp lệ cho nhân viên ID: " + emp.getId() + ", Department ID: " + emp.getDepartmentId());
                }
                countMap.put(departmentName, countMap.getOrDefault(departmentName, 0) + 1);
            }

            countMap.forEach(dataset::setValue);

        } catch (DataAccessException e) {
            LOGGER.log(Level.SEVERE, "Lỗi DataAccessException trong createEmployeePieChartPanel: " + e.getMessage(), e);
            panel.add(createErrorLabel("Không thể tải dữ liệu nhân viên. Chi tiết: " + e.getMessage()));
            return panel;
        } finally {
            closeDAO(employeeDAO);
            closeDAO(departmentDAO);
        }

        if (dataset.getItemCount() == 0) {
            panel.add(createErrorLabel("Không có dữ liệu nhân viên để hiển thị biểu đồ."));
            return panel;
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Phân Bổ Nhân Viên Theo Phòng Ban (" + totalEmployees + " nhân viên)", // Tiêu đề chính
                dataset,
                true,
                true,
                false
        );
        chart.setTitle(new org.jfree.chart.title.TextTitle("Phân Bổ Nhân Viên Theo Phòng Ban (" + totalEmployees + " nhân viên)", TITLE_FONT)); // Đặt lại tiêu đề với font
        stylePieChart(chart);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(900, 550)); // Giảm chiều cao một chút
        panel.add(chartPanel, BorderLayout.CENTER);
        return panel;
    }

    private void updateSalesBarChartPanel(String filter) {
        salesChartPanelContainer.remove(1); // Remove the old chart panel
        salesChartPanelContainer.add(createSalesBarChartPanel(filter), BorderLayout.CENTER);
        salesChartPanelContainer.revalidate();
        salesChartPanelContainer.repaint();
    }

    private JPanel createSalesBarChartPanel(String filter) {
        JPanel panel = createBasePanel();

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        SalesOrderDAO salesOrderDAO = null;

        try {
            salesOrderDAO = new SalesOrderDAO();
            List<SalesOrder> salesOrders = salesOrderDAO.getAll();

            Map<String, Double> salesByMonth = new HashMap<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
            LocalDate now = LocalDate.now();

            for (SalesOrder order : salesOrders) {
                LocalDate orderDate = order.getOrderDate();
                if (orderDate != null) {
                    boolean include = false;
                    switch (filter) {
                        case "Tháng này":
                            include = orderDate.getMonth() == now.getMonth() && orderDate.getYear() == now.getYear();
                            break;
                        case "Quý này":
                            int currentQuarter = (now.getMonthValue() - 1) / 3 + 1;
                            int orderQuarter = (orderDate.getMonthValue() - 1) / 3 + 1;
                            include = orderQuarter == currentQuarter && orderDate.getYear() == now.getYear();
                            break;
                        case "Năm nay":
                            include = orderDate.getYear() == now.getYear();
                            break;
                        case "Tất cả":
                            include = true;
                            break;
                    }

                    if (include) {
                        String monthYear = orderDate.format(formatter);
                        salesByMonth.put(monthYear, salesByMonth.getOrDefault(monthYear, 0.0) + order.getTotalAmount());
                    }
                }
            }

            salesByMonth.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> dataset.addValue(entry.getValue(), "Doanh số", entry.getKey()));

        } catch (DataAccessException e) {
            LOGGER.log(Level.SEVERE, "Lỗi DataAccessException trong createSalesBarChartPanel: " + e.getMessage(), e);
            panel.add(createErrorLabel("Không thể tải dữ liệu doanh số để hiển thị biểu đồ."));
            return panel;
        } finally {
            closeDAO(salesOrderDAO);
        }

        if (dataset.getRowCount() == 0) {
            panel.add(createErrorLabel("Không có dữ liệu doanh số để hiển thị biểu đồ."));
            return panel;
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Tổng Doanh Số Bán Hàng", // Tiêu đề chính
                "Tháng/Năm",
                "Doanh số (VNĐ)",
                dataset,
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        chart.setTitle(new org.jfree.chart.title.TextTitle("Tổng Doanh Số Bán Hàng", TITLE_FONT)); // Đặt lại tiêu đề với font
        styleBarChart(chart);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(900, 500)); // Giảm chiều cao một chút
        panel.add(chartPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createInventoryBarChartPanel() {
        JPanel panel = createBasePanel();

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        InventoryItemDAO inventoryItemDAO = null;

        try {
            inventoryItemDAO = new InventoryItemDAO();
            List<InventoryItem> items = inventoryItemDAO.getAll();

            for (InventoryItem item : items) {
                dataset.addValue(item.getQuantityInStock(), "Số lượng", item.getItemName());
            }

        } catch (DataAccessException e) {
            LOGGER.log(Level.SEVERE, "Lỗi DataAccessException trong createInventoryBarChartPanel: " + e.getMessage(), e);
            panel.add(createErrorLabel("Không có dữ liệu tồn kho để hiển thị biểu đồ."));
            return panel;
        } finally {
            closeDAO(inventoryItemDAO);
        }

        if (dataset.getRowCount() == 0) {
            panel.add(createErrorLabel("Không có dữ liệu tồn kho để hiển thị biểu đồ."));
            return panel;
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Số Lượng Hàng Tồn Kho Hiện Tại", // Tiêu đề chính
                "Mặt hàng",
                "Số lượng",
                dataset,
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        chart.setTitle(new org.jfree.chart.title.TextTitle("Số Lượng Hàng Tồn Kho Hiện Tại", TITLE_FONT)); // Đặt lại tiêu đề với font
        styleBarChart(chart);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(900, 550)); // Giảm chiều cao một chút
        panel.add(chartPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPurchaseOrderBarChartPanel() {
        JPanel panel = createBasePanel();

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        PurchaseOrderDAO purchaseOrderDAO = null;
        VendorDAO vendorDAO = null;

        try {
            purchaseOrderDAO = new PurchaseOrderDAO();
            vendorDAO = new VendorDAO();

            Map<Integer, String> vendorNames = vendorDAO.getAll().stream()
                    .collect(Collectors.toMap(Vendor::getId, Vendor::getVendorName));

            List<PurchaseOrder> purchaseOrders = purchaseOrderDAO.getAll();

            Map<String, Double> totalAmountByVendor = new HashMap<>();
            for (PurchaseOrder order : purchaseOrders) {
                String vendorName = "Không xác định";
                if (order.getVendorId() > 0 && vendorNames.containsKey(order.getVendorId())) {
                    vendorName = vendorNames.get(order.getVendorId());
                }
                totalAmountByVendor.put(vendorName, totalAmountByVendor.getOrDefault(vendorName, 0.0) + order.getTotalAmount());
            }

            totalAmountByVendor.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> dataset.addValue(entry.getValue(), "Tổng giá trị", entry.getKey()));

        } catch (DataAccessException e) {
            LOGGER.log(Level.SEVERE, "Lỗi DataAccessException trong createPurchaseOrderBarChartPanel: " + e.getMessage(), e);
            panel.add(createErrorLabel("Không có dữ liệu đơn hàng mua để hiển thị biểu đồ."));
            return panel;
        } finally {
            closeDAO(purchaseOrderDAO);
            closeDAO(vendorDAO);
        }

        if (dataset.getRowCount() == 0) {
            panel.add(createErrorLabel("Không có dữ liệu đơn hàng mua để hiển thị biểu đồ."));
            return panel;
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Tổng Giá Trị Đơn Hàng Mua Theo Nhà Cung Cấp", // Tiêu đề chính
                "Nhà cung cấp",
                "Tổng giá trị (VNĐ)",
                dataset,
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        chart.setTitle(new org.jfree.chart.title.TextTitle("Tổng Giá Trị Đơn Hàng Mua Theo Nhà Cung Cấp", TITLE_FONT)); // Đặt lại tiêu đề với font
        styleBarChart(chart);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(900, 550)); // Giảm chiều cao một chút
        panel.add(chartPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createProjectPieChartPanel() {
        JPanel panel = createBasePanel();

        DefaultPieDataset dataset = new DefaultPieDataset();
        ProjectDAO projectDAO = null;
        int totalProjects = 0;

        try {
            projectDAO = new ProjectDAO();
            List<Project> projects = projectDAO.getAll();
            totalProjects = projects.size();

            Map<String, Integer> statusCount = new HashMap<>();
            for (Project project : projects) {
                String status = project.getStatus() != null ? project.getStatus() : "Không xác định";
                statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
            }

            statusCount.forEach(dataset::setValue);

        } catch (DataAccessException e) {
            LOGGER.log(Level.SEVERE, "Lỗi DataAccessException trong createProjectPieChartPanel: " + e.getMessage(), e);
            panel.add(createErrorLabel("Không có dữ liệu dự án để hiển thị biểu đồ."));
            return panel;
        } finally {
            closeDAO(projectDAO);
        }

        if (dataset.getItemCount() == 0) {
            panel.add(createErrorLabel("Không có dữ liệu dự án để hiển thị biểu đồ."));
            return panel;
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Phân Bổ Dự Án Theo Trạng Thái (" + totalProjects + " dự án)", // Tiêu đề chính
                dataset,
                true,
                true,
                false
        );
        chart.setTitle(new org.jfree.chart.title.TextTitle("Phân Bổ Dự Án Theo Trạng Thái (" + totalProjects + " dự án)", TITLE_FONT)); // Đặt lại tiêu đề với font
        stylePieChart(chart);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(900, 550)); // Giảm chiều cao một chút
        panel.add(chartPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLeaveRequestPieChartPanel() {
        JPanel panel = createBasePanel();

        DefaultPieDataset dataset = new DefaultPieDataset();
        LeaveRequestDAO leaveRequestDAO = null;
        int totalRequests = 0;

        try {
            leaveRequestDAO = new LeaveRequestDAO();
            List<LeaveRequest> leaveRequests = leaveRequestDAO.getAll();
            totalRequests = leaveRequests.size();

            Map<String, Integer> statusCount = new HashMap<>();
            for (LeaveRequest request : leaveRequests) {
                String status = request.getStatus() != null ? request.getStatus() : "Không xác định";
                statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
            }

            statusCount.forEach(dataset::setValue);

        } catch (DataAccessException e) {
            LOGGER.log(Level.SEVERE, "Lỗi DataAccessException trong createLeaveRequestPieChartPanel: " + e.getMessage(), e);
            panel.add(createErrorLabel("Không có dữ liệu đơn nghỉ phép để hiển thị biểu đồ."));
            return panel;
        } finally {
            closeDAO(leaveRequestDAO);
        }

        if (dataset.getItemCount() == 0) {
            panel.add(createErrorLabel("Không có dữ liệu đơn nghỉ phép để hiển thị biểu đồ."));
            return panel;
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Phân Bổ Đơn Xin Nghỉ Phép Theo Trạng Thái (" + totalRequests + " đơn)", // Tiêu đề chính
                dataset,
                true,
                true,
                false
        );
        chart.setTitle(new org.jfree.chart.title.TextTitle("Phân Bổ Đơn Xin Nghỉ Phép Theo Trạng Thái (" + totalRequests + " đơn)", TITLE_FONT)); // Đặt lại tiêu đề với font
        stylePieChart(chart);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(900, 550)); // Giảm chiều cao một chút
        panel.add(chartPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTaskPieChartPanel() {
        JPanel panel = createBasePanel();

        DefaultPieDataset dataset = new DefaultPieDataset();
        TaskDAO taskDAO = null;
        int totalTasks = 0;

        try {
            taskDAO = new TaskDAO();
            List<Task> tasks = taskDAO.getAll();
            totalTasks = tasks.size();

            Map<String, Integer> statusCount = new HashMap<>();
            for (Task task : tasks) {
                String status = task.getStatus() != null ? task.getStatus() : "Không xác định";
                statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
            }

            statusCount.forEach(dataset::setValue);

        } catch (DataAccessException e) {
            LOGGER.log(Level.SEVERE, "Lỗi DataAccessException trong createTaskPieChartPanel: " + e.getMessage(), e);
            panel.add(createErrorLabel("Không có dữ liệu công việc để hiển thị biểu đồ."));
            return panel;
        } finally {
            closeDAO(taskDAO);
        }

        if (dataset.getItemCount() == 0) {
            panel.add(createErrorLabel("Không có dữ liệu công việc để hiển thị biểu đồ."));
            return panel;
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Phân Bổ Công Việc Theo Trạng Thái (" + totalTasks + " công việc)", // Tiêu đề chính
                dataset,
                true,
                true,
                false
        );
        chart.setTitle(new org.jfree.chart.title.TextTitle("Phân Bổ Công Việc Theo Trạng Thái (" + totalTasks + " công việc)", TITLE_FONT)); // Đặt lại tiêu đề với font
        stylePieChart(chart);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(900, 550)); // Giảm chiều cao một chút
        panel.add(chartPanel, BorderLayout.CENTER);
        return panel;
    }

    // Phần helper: tạo panel cơ bản
    private JPanel createBasePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        return panel;
    }

    // Phần helper: label báo lỗi
    private JLabel createErrorLabel(String message) {
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        label.setForeground(Color.RED.darker());
        return label;
    }

    // Phần helper: đóng DAO nếu không null
    private void closeDAO(Object dao) {
        if (dao != null) {
            try {
                dao.getClass().getMethod("close").invoke(dao);
            } catch (Exception ignored) {
            }
        }
    }

    // Phần helper: tạo style cho Pie Chart
    private void stylePieChart(JFreeChart chart) {
        chart.setBackgroundPaint(CHART_BG_COLOR);
        chart.getTitle().setFont(TITLE_FONT);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(CHART_BG_COLOR);
        plot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 14));
        plot.setLegendLabelGenerator(plot.getLegendLabelGenerator()); // Giữ legend mặc định
        plot.setLabelGap(0.02);
        plot.setSimpleLabels(true);
        plot.setOutlineVisible(false);
    }

    // Phần helper: tạo style cho Bar Chart
    private void styleBarChart(JFreeChart chart) {
        chart.setBackgroundPaint(CHART_BG_COLOR);
        chart.getTitle().setFont(TITLE_FONT);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.GRAY);

        plot.getDomainAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 14));
        plot.getRangeAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 14));
        plot.getDomainAxis().setLabelFont(TITLE_FONT);
        plot.getRangeAxis().setLabelFont(TITLE_FONT);

        if (plot.getRenderer() instanceof BarRenderer) {
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setBarPainter(new BarRenderer().getBarPainter());
            renderer.setSeriesPaint(0, new Color(79, 129, 189)); // Màu xanh dương dịu
        }
        chart.getLegend().setItemFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    // Main test chạy nhanh
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Giả lập user
            User user = new User();
            user.setUsername("Admin");

            new DashboardFrame(user);
        });
    }
}