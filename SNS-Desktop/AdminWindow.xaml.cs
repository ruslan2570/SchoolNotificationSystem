using System;
using System.Collections.Generic;
using System.Linq;
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
