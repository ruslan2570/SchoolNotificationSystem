using Newtonsoft.Json.Linq;
using SNS_Desktop.Model;
using System;
using System.IO;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;

namespace SNS_Desktop
{
	public partial class AuthWindow : Window
	{
		private readonly HttpClient client = new HttpClient();


		// Путь для сохранения файла с данными авторизации
		string FILENAME = System.Environment.GetEnvironmentVariable("USERPROFILE") + "\\Documents\\SCS_data";

		string host;
		string login;
		string password;

		enum LoginRole
		{
			Admin,
			User
		}

		public AuthWindow()
		{
			InitializeComponent();
			LoadAuthData();
			if (!File.Exists(FILENAME))
				File.Create(FILENAME);
		}

		private async void btnLogin_Click(object sender, RoutedEventArgs e)
		{
			host = tbHost.Text;
			login = tbLogin.Text;
			password = pbPass.Password;

			SaveAuthData();

			btnLogin.IsEnabled = false;
			btnAdmin.IsEnabled = false;

			await LoginTask(client, host, login, password,
				btnLogin, btnAdmin, LoginRole.User, this);
		}

		private async void btnAdmin_Click(object sender, RoutedEventArgs e)
		{
			host = tbHost.Text;
			login = tbLogin.Text;
			password = pbPass.Password;

			SaveAuthData();

			btnLogin.IsEnabled = false;
			btnAdmin.IsEnabled = false;

			await LoginTask(client, host, login, password,
				btnLogin, btnAdmin, LoginRole.Admin, this);
		}

		/* Ассинхронный метод для авторизации
		* Получает из запроса id сессии
		* Или ошибку
		*/
		private static async Task LoginTask(HttpClient client, string host, string login, string password,
			Button btnL, Button btnAdm, LoginRole role, Window currentWindow)
		{
			string stringTask = "";
			client.DefaultRequestHeaders.Accept.Clear();
			client.DefaultRequestHeaders.Accept.Add(
			new MediaTypeWithQualityHeaderValue("application/json"));
			client.DefaultRequestHeaders.Add("User-Agent", "SchoolNotificationSystem desktop");
			try
			{
				Uri uri = new Uri($"http://{host}/request?action=login&login={login}&password={password}");
				stringTask = await client.GetStringAsync(uri);
			}
			catch (Exception ex)
			{
				MessageBox.Show("Ошибка соединения", "Ошибка");
				return;
			}
			finally
			{
				btnL.IsEnabled = true;
				btnAdm.IsEnabled = true;
			}

			JObject jObject = JObject.Parse(stringTask);

			JToken tokenId = jObject["id"];

			if (tokenId == null)
			{
				JToken tokenError = jObject["error"];
				string errorMsg = tokenError.ToObject<string>();
				MessageBox.Show(errorMsg, "Ошибка");
				return;
			}

			string sessionId = tokenId.ToObject<string>();

			if (role == LoginRole.Admin)
			{
				JToken tokenUser = jObject["user"];
				User user = tokenUser.ToObject<User>();
				if (!user.roleName.Equals("Администратор"))
				{
					MessageBox.Show("Пользователь не является администратором", "Ошибка");
					return;
				}
			}

			if (role == LoginRole.Admin)
			{
				var win = new AdminWindow(host, sessionId);
				win.Show();
				currentWindow.Close();
			}
			else if (role == LoginRole.User)
			{
				var win = new UserWindow(host, sessionId);
				win.Show();
				currentWindow.Close();
			}
		}

		private void SaveAuthData()
		{
			string data = $"{host}\n{login}\n{password}";

			StreamWriter sw = new StreamWriter(FILENAME);
			sw.WriteLineAsync(data);
			sw.Close();
		}

		private void LoadAuthData()
		{
			if (!File.Exists(FILENAME))
				return;
			StreamReader sr = new StreamReader(FILENAME);
			tbHost.Text = sr.ReadLine();
			tbLogin.Text = sr.ReadLine();
			pbPass.Password = sr.ReadLine();
			sr.Close();
		}

		public void Typing(object sender, RoutedEventArgs e)
		{
			if (String.IsNullOrEmpty(tbHost.Text) || String.IsNullOrEmpty(tbLogin.Text) || String.IsNullOrEmpty(pbPass.Password))
			{
				btnLogin.IsEnabled = false;
				btnAdmin.IsEnabled = false;
			}
			else
			{
				btnLogin.IsEnabled = true;
				btnAdmin.IsEnabled = true;
			}

		}
	}
}
