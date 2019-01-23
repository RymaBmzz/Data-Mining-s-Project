package IHM;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import weka.core.Instances;


public class MaFenetre extends JFrame{
	
	String[] NomInstances = {
			"airline.arff", "breast-cancer.arff", "contact-lenses.arff", "cpu.arff",
			"cpu.with.vendor.arff", "credit-g.arff", "diabetes.arff", "glass.arff",
			"hypothyroid.arff", "ionosphere.arff", "iris.2D.arff", "iris.arff",
			"labor.arff", "ReutersCorn-test.arff", "ReutersCorn-train.arff",
			"ReutersGrain-test.arff", "ReutersGrain-train.arff", "segment-challenge.arff",
			"segment-test.arff", "soybean.arff", "supermarket.arff", "unbalanced.arff",
			"vote.arff", "weather.nominal.arff", "weather.numeric.arff"
			};

	private JLabel choixBenchmark_label, nb_attr_label, nb_clause_label, icon;
	private JComboBox<String> benchmark_comboBox;
	private JTextField nb_attr_textField, nb_clause_textField;
	private JButton afficher, quitter;
	private String DATASETPATH = null, nomFichier = null;
	private Thread t;
	
	public MaFenetre() {
		//définir un titre
		this.setTitle("IHM_TP_DM");
		//Définir la taille 500 pixels de large et 550 pixels de haut
		this.setSize(450,300);
		//positionner au centre
		this.setLocationRelativeTo(null);
		//terminer le processus lorsqu'on clique sur la croix rouge
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Choix d'un fichier
		JPanel panParcour = new JPanel();
		panParcour.setBackground(Color.WHITE);
		panParcour.setPreferredSize(new Dimension(300, 60));
		panParcour.setBorder(BorderFactory.createTitledBorder("Choix de benchmark"));
		benchmark_comboBox = new JComboBox<String>(NomInstances);
		JLabel choixInstances = new JLabel("fichiers .arff : ");
		panParcour.add(choixInstances);
		panParcour.add(benchmark_comboBox);
		
		//Icône
		icon = new JLabel(new ImageIcon("C:/Users/leader/Pictures/im2.jpg"));
		JPanel panIcon = new JPanel();
		panIcon.setBackground(Color.WHITE);
		panIcon.setLayout(new BorderLayout());
		panIcon.add(icon);
		
		//Affichage du nb d'attribut
		JPanel panNbClause = new JPanel();
		panNbClause.setBackground(Color.white);
		panNbClause.setPreferredSize(new Dimension(220, 60));
		nb_attr_textField = new JTextField();
		nb_attr_textField.setPreferredSize(new Dimension(100, 25));
		panNbClause.setBorder(BorderFactory.createTitledBorder("Nombre d'attributs"));
		nb_attr_label = new JLabel("Nombre : ");
		panNbClause.add(nb_attr_label);
		panNbClause.add(nb_attr_textField);
				
		//Affichage du nb de clauses
		JPanel panTaux = new JPanel();
		panTaux.setBackground(Color.white);
		panTaux.setPreferredSize(new Dimension(220, 60));
		nb_clause_textField= new JTextField();
		nb_clause_textField.setPreferredSize(new Dimension(100, 25));
		panTaux.setBorder(BorderFactory.createTitledBorder("Nombre de clauses"));
		nb_clause_label = new JLabel("Nombre : ");
		panTaux.add(nb_clause_label);
		panTaux.add(nb_clause_textField);
		
		JPanel content = new JPanel();
		content.setBackground(Color.white);
		content.add(panParcour);
		content.add(panNbClause);
		content.add(panTaux);
		
		//Boutton quitter
		JButton quitter = new JButton("Quitter");
		quitter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				/*
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				//t.stop();
				System.out.println("Arrêt de l'application");
				setVisible(false);
				dispose();
				*/
				t.stop();
				System.out.println("Arrêt de l'application");
				setVisible(false);
				dispose();	

			}
		});
		
		//Bouton Afficher
		afficher = new JButton("Afficher");
		
		afficher.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				t = new Thread(new LancerAlgorithme());
				t.setName("btnLancer");
				t.start();
				/*	
				nomFichier = getNomFichier();
				System.out.println("nom Fichier est " + nomFichier);
				DATASETPATH = "data/" + nomFichier;
				System.out.println("Chemin Fichier est " + DATASETPATH);
				LoadData mg = new LoadData();
				Instances dataset = null;
				try {
					dataset = mg.loadDataset(DATASETPATH);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Gui_LoadedData fenetre = new Gui_LoadedData();
				fenetre.Gui_OneDataset(dataset);
				fenetre.setVisible(true);

				Font police = new Font("Times New Roman",Font.BOLD,12);
				
				//nombre de clauses
				String nb_ligne= "" + dataset.numInstances();
				nb_clause_textField.setFont(police);
				nb_clause_textField.setForeground(Color.red);
				nb_clause_textField.setText(nb_ligne);
				
				
				//nombre de variables
				String nb_att = "" + dataset.numAttributes();
				nb_attr_textField.setFont(police);
				nb_attr_textField.setForeground(Color.red);
				nb_attr_textField.setText(nb_att);
				 */
			}
		});
		
		JPanel control = new JPanel();
		control.add(quitter);
		control.add(afficher);

		
		quitter.setMnemonic('Q');
		afficher.setMnemonic('A');
		
		this.getContentPane().add(panIcon, BorderLayout.WEST);
		this.getContentPane().add(content, BorderLayout.CENTER);
		this.getContentPane().add(control, BorderLayout.SOUTH);
		
		//rendre la fenetre visible
		this.setVisible(true);
	}
	
	class LancerAlgorithme implements Runnable{

		@Override
		public void run() {
			//nomFichier = getNomFichier();
			nomFichier = benchmark_comboBox.getSelectedItem().toString();
			System.out.println("nom Fichier est " + nomFichier);
			//DATASETPATH = "data/" + nomFichier;
			DATASETPATH = "./data/" + nomFichier;
			System.out.println("Chemin Fichier est " + DATASETPATH);
			LoadData mg = new LoadData();
			Instances dataset = null;
			try {
				dataset = mg.loadDataset(DATASETPATH);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Gui_LoadedData fenetre = new Gui_LoadedData();
			fenetre.Gui_OneDataset(dataset);
			fenetre.setVisible(true);

			Font police = new Font("Times New Roman",Font.BOLD,12);
			
			//nombre de clauses
			String nb_ligne= Integer.toString(dataset.numInstances());
			nb_clause_textField.setFont(police);
			nb_clause_textField.setForeground(Color.red);
			nb_clause_textField.setText(nb_ligne);
			
			
			//nombre de variables
			String nb_att = Integer.toString(dataset.numAttributes());
			nb_attr_textField.setFont(police);
			nb_attr_textField.setForeground(Color.red);
			nb_attr_textField.setText(nb_att);	

		}
	}
	
	public String getNomFichier(){
		System.out.println("selected Item is " + (String)benchmark_comboBox.getSelectedItem());
		return (String)benchmark_comboBox.getSelectedItem();
		
	}

}
