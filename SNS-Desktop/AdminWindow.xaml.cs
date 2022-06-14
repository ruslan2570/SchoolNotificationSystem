using System.Windows;

namespace SNS_Desktop
{
	/// <summary>
	/// Interaction logic for AdminWindow.xaml
	/// </summary>
	public partial class AdminWindow : Window
	{
		string host;
		string sessionId;
		public AdminWindow(string host, string sessionId)
		{
			InitializeComponent();
			this.sessionId = sessionId;
			this.host = host;
		}

		private void Window_Closing(object sender, System.ComponentModel.CancelEventArgs e)
		{
			new AuthWindow().Show();
		}

		private void btnMessages_Click(object sender, RoutedEventArgs e)
		{
			frmAdmin.Navigate(new AdminPages.MessageManagement(host, sessionId));
		}

		private void btnUsers_Click(object sender, RoutedEventArgs e)
		{
			frmAdmin.Navigate(new AdminPages.UserManagement(host, sessionId));
		}
	}
}
