using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace SNS_Desktop
{
	public partial class AuthWindow : Window
	{
		private readonly HttpClient client = new HttpClient();

		string FILENAME = "authdata.txt";

		string host;
		string login;
		string password;
		
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

			await LoginTask(client, host, login, password, btnLogin, btnAdmin);
		}

		private async void btnAdmin_Click(object sender, RoutedEventArgs e)
		{
			host = tbHost.Text;
			login = tbLogin.Text;
			password = pbPass.Password;

			SaveAuthData();

			btnLogin.IsEnabled = false;
			btnAdmin.IsEnabled = false;

			await LoginTask(client, host, login, password, btnLogin, btnAdmin);
		}

		private static async Task LoginTask(HttpClient client, string host, string login, string password, 
			Button btnL, Button btnAdm)
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
			} catch(Exception ex)
			{
				MessageBox.Show("Ошибка соединения", "Ошибка");
			}
			finally
			{
				btnL.IsEnabled = true;
				btnAdm.IsEnabled = true;
			}

			MessageBox.Show(stringTask);
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
