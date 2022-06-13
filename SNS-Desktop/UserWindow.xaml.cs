using Newtonsoft.Json.Linq;
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
using System.Windows.Shapes;

namespace SNS_Desktop
{
	/// <summary>
	/// Interaction logic for UserWindow.xaml
	/// </summary>
	public partial class UserWindow : Window
	{
		private readonly HttpClient client = new HttpClient();

		string sessionId;
		string host;
		public UserWindow(string host, string sessionId)
		{
			InitializeComponent();
			this.host = host;
			this.sessionId = sessionId;
		}

		

		private async void btnSend_Click(object sender, RoutedEventArgs e)
		{
			string message = tbMessage.Text;
			if (String.IsNullOrWhiteSpace(message))
			{
				MessageBox.Show("Введите сообщение", "Ошибка");
				return;
			}

			client.DefaultRequestHeaders.Accept.Clear();
			client.DefaultRequestHeaders.Accept.Add(
			new MediaTypeWithQualityHeaderValue("application/json"));
			client.DefaultRequestHeaders.Add("User-Agent", "SchoolNotificationSystem desktop");
			string result = "";
			try
			{
				Uri uri = new Uri($"http://{host}/request?action=sendMessage&session_id={sessionId}&text={message}");
				result = await client.GetStringAsync(uri);
			}
			catch (Exception ex)
			{
				MessageBox.Show("Ошибка соединения", "Ошибка");
				return;
			}

			JObject jObject = JObject.Parse(result);
			JToken tokenSuccess = jObject["success"];
			JToken tokenError = jObject["error"];
			string msg = "Неизвестная ошибка";
			if (tokenSuccess != null)
			{
				msg = tokenSuccess.ToObject<string>();
			}
			else if(tokenError != null)
			{
				msg = tokenError.ToObject<string>();
			}

			MessageBox.Show(msg, "Ответ");
			tbMessage.Text = "";
		}

		private void btnClear_Click(object sender, RoutedEventArgs e)
		{
			tbMessage.Text = "";
		}

		private void Window_Closing(object sender, System.ComponentModel.CancelEventArgs e)
		{
			new AuthWindow().Show();
		}
	}
}
