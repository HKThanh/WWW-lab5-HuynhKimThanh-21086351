# LAB 05 - LẬP TRÌNH WWW (JAVA)

## Sinh viên thực hiện
- **Họ và tên: Huỳnh Kim Thành** 
- **MSSV: 21086351** 

---

## Tech stack
- **Frontend:** HTML, CSS, JavaScript, Bootstrap
- **Backend:** Java, Spring Boot, Spring Data JPA, MariaDB, Python
- **Database:** HeidiSQL
- **IDE:** IntelliJ IDEA
- **Version control:** Git, GitHub
- **Documentation:** Markdown
- **Others:** Lombok, FasterXML, neovisionaries,...

---

## Tính năng
1. **Đăng nhập phân quyền bằng RequestMapping**
   - Candidate đăng nhập: Hiển thị các Job gợi ý.
   - Company đăng nhập: Quản lý Tin đăng ứng tuyển.
2. **Hiển thị danh sách các ứng viên.**
3. **Hiển thị danh sách các ứng viên bằng pagination.**
4. **Gợi ý công việc cho ứng viên dựa trên kĩ năng (Có áp dụng machine learning).**
5. **Thêm ứng viên mới.**
6. **Sửa thông tin ứng viên.**
7. **Thêm company**
8. **Gợi ý ứng viên dành cho company**
9. **Lọc ứng viên theo kĩ năng**

---

## Hướng dẫn sử dụng

1. **Clone project từ GitHub về máy tính bằng lệnh:**
   ```bash
   https://github.com/HKThanh/WWW-lab5-HuynhKimThanh-21086351.git
   ```

2. **Mở project bằng IntelliJ IDEA:**
   - Đảm bảo đã cài đặt các plugin như Lombok, Spring Boot, Spring Data JPA, Python, ...

3. **Mở HeidiSQL và thực hiện:**
   - Chạy file `works.sql`.

4. **Chỉnh sửa thông tin kết nối database trong file `application.properties`.**

5. **Chạy project:**
   - Truy cập vào đường dẫn:
     - [http://localhost:8080](http://localhost:8080)

6. **Bắt đầu sử dụng các tính năng của ứng dụng.**

---

## Một số hình ảnh minh họa

### Trang chủ:
![Trang chủ](https://drive.google.com/uc?id=1RRuPdm5NqcUK_f5Bs10pNFfwEvEtuBud)

### Trang đăng nhập (ứng viên):
![Login](https://drive.google.com/uc?id=1vLMCwqn1yfBVwatIobo62rILomvIznkm)

### Trang đăng ký (ứng viên):
![Register](https://drive.google.com/uc?id=1HCWV9CS6mT3UxJjN5rwQWigNNccpBLbp)

### Trang thông tin ứng viên với đề xuất công việc:
![Thông tin](https://drive.google.com/uc?id=17m1qMme6lJ6ICGmLaw-zMTXZvJYld6MY)

### Trang dashboard của công ty:
![Dashboard](https://drive.google.com/uc?id=1Jx3wW6bThTkhQ4fNPGGEXIrf8P_s1qMS)

### Trang tìm kiếm ứng viên:
![Tìm kiếm](https://drive.google.com/uc?id=1Fkw5P8384tNaCfbiVVwwHVOXXQFIof7w)
