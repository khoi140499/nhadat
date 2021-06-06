

1

<center> <h1>GIỚI THIỆU VỀ BACK4APP</h1> </center>

**1.1. Giới thiệu**

Back4app là một mã nguồn mở dựa trên nền tảng Parse Server– dịch vụ đám mây đa nền

tảng hay còn gọi là một framework Backend-as-a-Servicesử dụng được với hầu hết các ngôn ngữ

lập trình thông dụng hiện nay như Java, Swift, Javascript, PHP, .Net,... Với các dịch vụ này, các

thiết bị di động, website có thể kết nối tới các API và file store đi kèm với nhiều tính năng như

xác thực, đẩy thông báo, tích hợp mạng xã hội như Facebook, Google, Twitter,.. phântích dữ liệu.

Với việc chỉ sử dụng một API và một SDK duy nhất khiến cho việc quản lý cơ sở dữ liệu

trở nên đơn giản hơn và có thể c hia sẻ qua nền tảng đám mây. Từ đó việc xây dựng hệ thống

backend cho ứng dụng sẽ không còn phức tạp.

Ngoài ra, Back4app có thể thêm nhiều ngườidùng vào một tài khoản. Bằngcách này các

thành viên trong nhóm có quyềntruy cập vào một tài khoản duy nhất giúp cho hiệu quả công việc

được nâng cao. Với mỗi tài khoản ta có thể tạo ra nhiều application trên Back4App vớigói bas ic

1Gb lưu trữ cùng với 30request/s.

Có thể thực hiện các hoạt động CRUID (tạo, đọc, cập nhật và xóa) thông qua Back4App

bằng cách gọi các API Object. Mỗ i Object bao gồm các tập hợp các cặp key-value được truyền

thông qua API dưới dạng JSON. Các key phảilà chuỗi và chữ số, giá trị phải là các giá trị như

c huỗ i, số, ...được mã hóa JSON. Các Object sẽ được tổ chức theo các lớp để có thể phân loại dữ

liệu. Để xử lý, ta chỉ cần sử dụng Parse để phân tích các cú pháp và API.

**1.2. Các tínhnăng**

\- Push Notification:Vớitính năng này, đội ngũ phát triển ứng dụng có thể đẩy thông báo từ

trên hệ thống, c loud code hoặc từ c lien t

*Hình 1. Push notication*

\- Dashboard: Giúp cho đội ngũ phát triển có thể dễ dàng quản lý các thành phần có trong mã

nguồn như API, Database, App setting,..





2

*Hình 2. Dashboard Back4App*

\- Manager parse server: Thay đổi phiên bản làm việc của ParseServer

*Hình 3. Manager parse server*

\- Core setting: Có thể thay đổi các thiết đặt về key ứng dụng, api,... hoặc chỉnh sửa, xóa ứng

dụng.





3

*Hình 4, Core setting*

\- Logs: Theo dỗi được log server và log system một cách trực quan nhất.

*Hình 5. Log Back4App*

\- Live query:Tùy chỉnh các cài đặt Server URL để sử dụng cho real time database





4

*Hình 6. Live Query trong back4App*

\- Verticate : Giúp cho việc xác minh các email tài khoản là hợp lệ.

*Hình 7. Chức năng xác thực email*

\- Clo ud code: Cho phép tải lên, gỡ lỗi và chạy các mã Javascript tùychỉnh vớiBack4App





5

*Hình 8. Cloud code trên Back4App*

\- Background job: Giúp cho việc các công việc cụ thể có thể chạy trên nền ứng dụng

*Hình 9. Background job*

\- Social lo gin : Hỗ trợ cấu hình ứng dụng với các mạng xã hội thông dụng

*Hình 10. Social login trong Back4App*





6

**ỨNG DỤNG NHÀ ĐẤT**

**2.1. Mô tả ứng dụng**

Ứng dụng nhà đất sử dụng ngôn ngữ java kết hợp với Back4App để lưu trữ dữ liệu trực

tuyến. Ứng dụng có các chức năng như xem tin đăng, quản lý tin đăng, đăng nhập, đăng ký, tìm

kiếm.

\- Link database: https:/[/www.back4app.com/database/khoi14041999/nh-t](http://www.back4app.com/database/khoi14041999/nh-t)

**2.2. Mô tả chức năngchính**

**2.2.1.** Kiểm tra kết nối mạng và tài khoản đăng nhập

Sau khi khởi động ứng dụng Vvewgroup hiển thị animat io n lo go sẽ được hiển thị, sau khi

animation kết thúc sẽ thực hiện ẩn viewgroup chứa logo, tại đây sẽ thực hiện kiểm tra kết nối

mạng, nếu không có kết nối mạng ứng dụng sẽ chuyển qua layout ErrorPagengược lại nếu có kết

nối mạng sẽ thực hiện tiếp kiểm tra tài khoản đăng nhập nếu tài khoản đăng nhập là admin, ứng

dụng sẽ được chuyển sang layout Admin nếu không, hoặc là tài khoản thường thì chuyển về

layoutchính của ứng dụng và thực hiện query hiển thị ảnh p ro file và các tin đăng.

*Hình 11. Kiểm tra đăng nhập và trạng thái kết nối mạng*





7

**2.2.2. Đăng Nhập**

Ứng dụng sẽ kiểm tra nếu người dùng không nhập username, passwordkhi nhấn button

Đăng Nhập sẽ hiển thị error trên edittext yêu cầu ngườidùng nhập đầy đủ thông tin. Sau khi nhập

đầy đủ, ứng dụng sẽ kiểm tra username, password vàxác thực email nếu người dùng chưa xác

thực email thì sẽ không được vào hệ thống ngược lại ngườidùng sẽ được quay trở lại màn hình

chính. Nếu username là admin thì ứng dụng chuyển đến layout admin.

*Hình 12. Chức năng đăng nhập*

**2.2.3. Đăng ký**

Tương tự như phần đăng nhập, ứng dụng sẽ kiểm tra các trường trống và kiểm tra đ ịnh

dạng email. Sau đó ứng dụng sẽ kiểm tra xem email đăng ký đã có trong database hay chưa. Nếu

chưa ứng dụng hiển thị hộp thoại xác thực email và chuyển qua layout đăng nhập với username và

password vừa được đăng ký, tuy nhiên cần xác thực email trước khi đăng ký. Nếu email đã tồn

tại, người dùng sẽ phải nhập lại.





8

*Hình 13. Chức năng đăng ký*

**2.2.4. Reset Password**

Người dùng chọn reset password trên layout đăng nhập, ứng dụng hiển thị layput nhập

email. Ứng dụng sẽ gửi email reset password đến cho người dùng.

*Hình 14. Chức năng lấy lại mật khẩu*

**2.2.5. Tìm kiếm**

Trên layout MainActivity nhập chuỗi trên thanh SearchView, ứng dụng sẽ tìm kiếm gần

đúng theo tiêu đề trong database, nếu có các tin đăng sẽ được hiển thị ở recycle view bên dưới.

Mỗi tin đăng sẽ bao gồm ảnh, tiêu đề, giá, tên người đăng, thời gian đăng, địa điểm. Nếu không

có kết quả trả về sẽ không có tin đăng nào được hiển thị ở RecycleView.





9

*Hình 15. Chức năng tìm kiếm*

**2.2.6. Xem tin đăng**

Trên màn hình chính, chọn tin đăng bất kì ứng dụng sẽ gửi object TinDang sang layout xem

thông tin chi tiết để hiển thị, đồng thời số lượt xem sẽ được tăng lên 1. Ở màn hình xem thông tin

chi tiết sẽ có s lide ảnh sử dụng thư viện ngoài đó là SliderImage, các Textview giá, địa chỉ, tiêu

đề, CircleImage profile user post, tên người post, trungbình cộng đánh giá, Button xem trang cá

nhân, mô tả và các phương thức liên lạc như là Button điện thoại và ButtonNhắn tin khi nhấn vào

các Button này trình gọi đ iện và nhắn tin mặc đ ịnh sẽ được hiển thị với số đ iện thoại của user

được truyền vào.





10

*Hình 16. Chức năng xem tin đăng*

**2.2.7. Xem thông tin profile**

Với chức năng này, ngườidùng sẽ được cung cấp tên đầy đủ, ảnh profile, thờigian tham

giam email, số đ iện thoại, địa chỉ và danh sách các tin đăng đã được lưu. Các dữ liệu đều được

ứng dụng query từ usernameđược lưu trong session trên máy.

*Hình 17, Chức năng xem thông tin profile*

**2.2.8. Chỉnh sửa thông tin profile**





11

Người dùng sẽ chọn ảnh, ảnh sẽ được lưu trên Firebase storage đồng thời sẽ lấy ra url để

lưu trên database cùng với các trường khác được người dùng sửa trong form. Ứng dụng sẽ update

theo username và thông báo kết quả cho người dùng

*Hình 18. Chức năng chỉnh sửa profile*

**2.2.9. Lưu tinđăng**

Trên danh sách các tin đăng, để lưu tin đăng ngườidùng nhấn vào hình trái tim viền đỏ để

thực hiện lưu tin ở đây ứng dụng sẽ kiểm tra nếu chưa đăng nhập sẽ hieent thị thông báo chưa

đăng nhập hoặc ngườidùng k thể lưu tin đăng của chính mì nh cuối cùng tin được lưu sẽ được cập

nhật trên database và đồng thời sau khi khởi động lại ứng dụng tin được lưu vừa rồi sẽ được set

hình trái tim đặc màuđỏ. Muốn hủy lưu ngườidùng chỉ cần nhấp vào hình trái tim, tin đăng được

lưu sẽ được xóa khỏidatabase.





12

*Hình 19. Chức năng lưu tin*

**2.2.10.**

**Đánh giá**

Sau khi vào layoutđánh giá, ứng dụng sẽ query dữ liệu từ người dùng để hiển thị ảnh

profile, tên đầy đủ. Ngườidùng sẽ chọn số lượng sao đánh giá và nhập commentđể đánh giá. Sau

khi nhấn Button đánh giá, ứng dụng kiểm tra xem có tài khoản nào đăng nhập hay không, nếu trả

về là null thì sẽ không được đánh giá nếu có ứng dụng sẽ lưu đánh giá vào database đông thời sẽ

put lên thông báo với chanel được set là tên username của người post tin đăng và hiển thị thông

báo đánh giá thành công.





13

*Hình 20. Chức năng đánh giá*

**2.2.11.**

**Xem trang cá nhân**

Ứng dụng sẽ truy xuất cơ sở dữ liệu với tên ngườidùng tương ứng để hiển thị ảnh

profile, tên đầy đủ, tổng trung bình các đánh giá từ ngườixem. Các thông tin về thời gian tham

gia, địa chỉ và danh sách tin đăng sẽ được hiển thị.

*Hình 21. Chức năng xem trang cá nhân*

**2.2.12.**

**Đăng tin**

Người dùng thực hiện nhập ảnh và ảnh sẽ được tải lên Firebase Storage đồng thời sẽ lấy





14

ra url ảnh để lưu cùng các thông tin trên form và ứng dụng sẽ lưu lại tin đăng trên database với

tình trạnglà chưa duyệt.

*Hình 22. Chức năng đăng tin*

**2.2.13.**

**Quản lý tinđăng**

Ngườidùng có thể quản lý được các tin đăngvà thực hiện các hành động với từng tin

đăng đó, người dùng có thể ẩn, chỉnh sửa và xóa tin đăng, đối với các chức năng ẩn và xóa ứng

dụng sẽ cập nhậttình trạng tin đăng đồng thời tin đăng sẽ khôngđược hiển thị trên danh sách tin

đăng. Với chức năng chỉnh sửa, ngườidùng sẽ được chuyểnsang layoutchỉnh sửa và nhập các

thông tin trên form, ứng dụng sẽ cập nhật trên database đồng thời chuyển tình trạnglà chưa duyệt.

Ở với mỗi tin đều có thể thực hiện với 3 chức năng kể trên. Ngoài ra ứng dụng sẽ kiểm tra xem có

tài khoản nào đăng nhập hay khôngnếu không ViewGroup yêu cầu đăng nhập/đăng ký sẽ được

hiển thị và ViewGroup quảnlý tin đăng sẽ bị ẩn.

*Hình 23. Chức năng quản lý tin đăng*





15

**2.2.14.**

**Duyệt tin đăng**

Các tin đăng chưa được duyệt sẽ được hiển thị trên recycle view, adminsẽ nhấn nút tích

xanh để thực hiện duyệt, ứng dụng sẽ cập nhật tình trạng duyệt và sẽ put cho user được duyệt

thông báo duyệttin thành công đồng thời tin đăng sẽ bị xóa khỏi danhsách

*Hình 24. Chức năng duyệt tin đăng*

**2.2.15.**

**Thông báo đánh giá, duyệttin.**

Sau khi mở ứng dụng, người dùng sẽ nhận được thông báo đánh giá hoặc duyệt tin theo

channel ứng với tên user

*Hình 25. Chức năng thông báo*

