using System;
using System.Collections.Generic;
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

		
		public AuthWindow()
		{
			InitializeComponent();
			btnLogin.IsEnabled = false;
		}

		private async void btnLogin_Click(object sender, RoutedEventArgs e)
		{
			string host = tbHost.Text;
			string login = tbLogin.Text;
			string password = pbPass.Password;

			await LoginTask(client, host, login, password);
		}

		private async void btnAdmin_Click(object sender, RoutedEventArgs e)
		{
			string host = tbHost.Text;
			string login = tbLogin.Text;
			string password = pbPass.Password;

			await LoginTask(client, host, login, password);
		}

		private static async Task LoginTask(HttpClient client, string host, string login, string password)
		{
			client.DefaultRequestHeaders.Accept.Clear();
			client.DefaultRequestHeaders.Accept.Add(
			new MediaTypeWithQualityHeaderValue("application/json"));
			client.DefaultRequestHeaders.Add("User-Agent", "SchoolNotificationSystem desktop");
			Uri uri = new Uri($"http://{host}/request?action=login&login={login}&password={password}");
			var stringTask = client.GetStringAsync(uri);

			var msg = await stringTask;
			MessageBox.Show(msg);
		}

		public void Typing(object sender, RoutedEventArgs e)
		{
			if(String.IsNullOrEmpty(tbHost.Text) || String.IsNullOrEmpty(tbLogin.Text) || String.IsNullOrEmpty(pbPass.Password))
				btnLogin.IsEnabled = false;
			else
				btnLogin.IsEnabled = true;
		}

		
	}
}
