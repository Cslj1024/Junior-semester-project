package com.aiit.lj;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class LibrarySystem {
	
    private static Map<String, Admin> adminDatabase = new HashMap<>();
    private static Map<String, Reader> readerDatabase = new HashMap<>();

    private static final String ADMIN_FILE = "admins.dat";
    private static final String READERS_FILE = "readers.dat";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        File adminsFile = new File(ADMIN_FILE);
        if (!adminsFile.exists()) {
            try {
                adminsFile.createNewFile();
                System.out.println("已创建管理员数据文件 'admins.dat'");
            } catch (IOException e) {
                System.out.println("无法创建管理员数据文件 'admins.dat'");
                e.printStackTrace();
                System.exit(1); // 终止程序
            }
        }

        File readersFile = new File(READERS_FILE);
        if (!readersFile.exists()) {
            try {
                readersFile.createNewFile();
                System.out.println("已创建读者数据文件 'readers.dat'");
            } catch (IOException e) {
                System.out.println("无法创建读者数据文件 'readers.dat'");
                e.printStackTrace();
                System.exit(1); // 终止程序
            }
        }

        loadAdmins();
        loadReaders();

        while (true) {
            System.out.println("欢迎使用图书馆登录系统！");
            System.out.println("1. 管理员登录");
            System.out.println("2. 读者注册");
            System.out.println("3. 读者登录");
            System.out.println("4. 退出");
            System.out.print("请选择操作：");

            int choice=0;

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // 消耗换行符
            } catch (InputMismatchException e) {
                // 处理输入不是整数的情况
                System.out.println("无效的选择，请重新输入整数选择。");
                scanner.nextLine(); // 清除无效输入
                continue;
            }

            switch (choice) {
                case 1:
                    adminLogin();
                    break;
                case 2:
                    readerRegister();
                    break;
                case 3:
                    readerLogin();
                    break;
                case 4:
                    saveAdmins();
                    saveReaders();
                    System.out.println("感谢使用图书馆登录系统！");
                    System.exit(0);
                    break;
                default:
                    System.out.println("无效的选择，请重新输入。");
            }
        }
    }

    private static void loadAdmins() {

        try (BufferedReader reader = new BufferedReader(new FileReader(ADMIN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String loginName = parts[0];
                    String username = parts[1];
                    String password = parts[2];
                    Admin admin = new Admin(loginName, username, password);
                    adminDatabase.put(loginName, admin);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveAdmins() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ADMIN_FILE))) {
            for (Admin admin : adminDatabase.values()) {
                writer.write(admin.getLoginName() + "," + admin.getUsername() + "," + admin.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadReaders() {
        try (BufferedReader reader = new BufferedReader(new FileReader(READERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    int id = Integer.parseInt(parts[0]);
                    String loginName = parts[1];
                    String username = parts[2];
                    String gender = parts[3];
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date birthday = dateFormat.parse(parts[4]);
                    Date registTime = dateFormat.parse(parts[5]);
                    Reader readerObj = new Reader(id, loginName, username, gender, birthday, registTime);
                    readerDatabase.put(loginName, readerObj);
                }
            }
        } catch (IOException | java.text.ParseException e) {
            e.printStackTrace();
        }
    }

    private static void saveReaders() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(READERS_FILE))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (Reader reader : readerDatabase.values()) {
                writer.write(
                    reader.getId() + "," +
                    reader.getLoginName() + "," +
                    reader.getUsername() + "," +
                    reader.getGender() + "," +
                    dateFormat.format(reader.getBirthday()) + "," +
                    dateFormat.format(reader.getRegistTime())
                );
                writer.newLine();
            }

            // 刷新缓冲区并关闭文件
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void adminLogin() {
        System.out.print("请输入管理员用户名：");
        String username = scanner.nextLine();
        System.out.print("请输入管理员密码：");
        String password = scanner.nextLine();

        Admin admin = adminDatabase.get(username);

        if (admin != null && admin.getPassword().equals(password)) {
            System.out.println("管理员登录成功！");
        } else {
            System.out.println("管理员登录失败，请检查用户名和密码。");
        }
    }

    private static boolean isValidLoginName(String loginName) {
        // 检查长度是否符合要求，以及是否仅包含字母和数字
        return loginName.length() >= 3 && loginName.length() <= 6 && loginName.matches("^[a-zA-Z0-9]*$");
    }

    private static boolean isValidPassword(String password) {
        // 检查密码长度是否为6位，并且是否由数字和英文字母组成
        return password.length() == 6 && password.matches("^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z0-9]+$");
    }

    private static void readerRegister() {
        System.out.print("请输入登录名（3-6位字母和数字组合）：");
        String loginName = scanner.nextLine();

        if (!isValidLoginName(loginName)) {
            System.out.println("登录名不符合要求！");
            return;
        }

        System.out.print("请输入用户名：");
        String username = scanner.nextLine();

        System.out.print("请输入密码 [6位字母和数字组合]：");
        String password = scanner.nextLine();

        if (!isValidPassword(password)) {
            System.out.println("密码不符合要求！");
            return;
        }


        System.out.print("请输入性别：");
        String gender = scanner.nextLine();

        System.out.print("请输入生日（格式：yyyy-MM-dd）：");
        String birthdayString = scanner.nextLine();
        Date birthday = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            birthday = dateFormat.parse(birthdayString);
        } catch (Exception e) {
            System.out.println("生日格式不正确！");
            return;
        }

        Date registTime = new Date();

        if (readerDatabase.containsKey(loginName)) {
            System.out.println("登录名已经存在，请选择另一个登录名。");
            return;
        }


        // 创建新的 Reader 对象时，将用户输入的密码设置为对象的密码属性
           Reader newReader = new Reader(readerDatabase.size() + 1, loginName, username, gender, birthday, registTime);
           newReader.setPassword(password); // 设置密码

           // 将新的 Reader 对象添加到 readerDatabase 中
           readerDatabase.put(loginName, newReader);

        saveReaders();
        System.out.println("注册成功！");
    }


    private static void readerLogin() {
        System.out.print("请输入登录名：");
        String loginName = scanner.nextLine();
        System.out.print("请输入密码：");
        String password = scanner.nextLine();

        Reader reader = readerDatabase.get(loginName);



        System.out.println(reader.getPassword());

        if (reader != null && password.equals(reader.getPassword())) {
            System.out.println("读者登录成功！");
            displayReaderInfo(reader);
        } else {
            System.out.println("登录失败，请检查登录名和密码。");
        }
    }

    private static void displayReaderInfo(Reader reader) {
        System.out.println("读者信息：");
        System.out.println("ID: " + reader.getId());
        System.out.println("登录名：" + reader.getLoginName());
        System.out.println("用户名：" + reader.getUsername());
        System.out.println("性别：" + reader.getGender());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("生日：" + dateFormat.format(reader.getBirthday()));
        System.out.println("注册时间：" + dateFormat.format(reader.getRegistTime()));
    }
}
